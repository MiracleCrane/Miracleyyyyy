/*
 * 文 件 名:  ImportExportImplService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.service.impl;

import com.huawei.smartcampus.datatool.base.enumeration.ExportTypeAndFileNameEnum;
import com.huawei.smartcampus.datatool.base.handler.exportdata.base.ExportHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.factory.ExportHandlerFactory;
import com.huawei.smartcampus.datatool.base.handler.importdata.helper.ImportHelper;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.service.ImportExportService;
import com.huawei.smartcampus.datatool.base.utils.CheckUtils;
import com.huawei.smartcampus.datatool.base.utils.CommonUtils;
import com.huawei.smartcampus.datatool.base.utils.FileOperateUtils;
import com.huawei.smartcampus.datatool.base.utils.QueueUtils;
import com.huawei.smartcampus.datatool.base.vo.Detail;
import com.huawei.smartcampus.datatool.base.vo.DetailNumber;
import com.huawei.smartcampus.datatool.base.vo.History;
import com.huawei.smartcampus.datatool.base.vo.req.ExportReq;
import com.huawei.smartcampus.datatool.base.vo.req.ImportHistoryReq;
import com.huawei.smartcampus.datatool.base.vo.req.ImportReq;
import com.huawei.smartcampus.datatool.base.vo.resp.ImportDetailResp;
import com.huawei.smartcampus.datatool.base.vo.resp.ImportHistoryResp;
import com.huawei.smartcampus.datatool.entity.ImportDetailEntity;
import com.huawei.smartcampus.datatool.entity.ImportHistoryEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.i18n.I18nUtils;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.repository.ImportHistoryRepository;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.RequestContext;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.utils.TimeUtil;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 导入导出实现类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Service
public class ImportExportImplService implements ImportExportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportImplService.class);

    @Autowired
    private QueueUtils queueUtils;

    @Autowired
    private ImportHelper importHelper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ImportHistoryRepository importHistoryRepository;

    @Override
    public BaseResponse importAsync(ImportReq importReq, HttpServletRequest request) {
        // 创建临时目录
        String path = FileOperateUtils.generateTemporaryDirectoryName();
        try {
            // 校验参数
            CheckUtils.checkImportParams(importReq);
            // 解析压缩包
            String zipFileTempName = UUID.randomUUID().toString().substring(0, 32) + ".zip";
            // 解压压缩文件，到path下
            FileOperateUtils.unzipFile(importReq.getResource(), path, zipFileTempName);
            // 遍历文件获取所有文件的绝对路径
            List<String> filePaths = FileOperateUtils.listFiles(path);
            // 存储一条数据到history表，返回taskId给前端，用于查询状态
            HashMap<String, String> hashMap = new HashMap<>();
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            String taskId = importHelper.saveImportHistoryEntity(importReq, filePaths.size());
            // 存储后，立刻提交
            transactionManager.commit(status);
            hashMap.put("taskId", taskId);
            // json获取对象信息
            int index = 1;
            for (String filepath : filePaths) {
                TaskModel taskModel = new TaskModel(taskId, importReq.getResourceOrigin(), importReq.getResourceType(),
                        CommonUtils.importMode(importReq), CommonUtils.isSkip(importReq.getDuplicatePolicy()),
                        filePaths.size(), RequestContext.getUserName(),
                        (String) RequestContext.get(RequestContext.USER_TYPE_FIELD), LocaleContextHolder.getLocale());
                taskModel.setFileSuffix(FileOperateUtils.getFileSuffix(filepath));
                taskModel.setFileName(FileOperateUtils.getFileNameWithoutSuffix(filepath));
                taskModel.setNumber(index++);
                taskModel.setSingleFile(true);
                Optional<String> jsonFromFile = FileOperateUtils.readJsonFromFile(filepath);
                if (jsonFromFile.isPresent()) {
                    taskModel.setFileContent(jsonFromFile.get());
                }
                // 将taskModel放到队列中
                queueUtils.addTaskModel(taskModel);
            }
            return BaseResponse.newOk(hashMap);
        } finally {
            // 删除临时文件夹和文件内容
            FileOperateUtils.deleteDirectory(new File(path));
        }
    }

    @Override
    public void export(ExportReq exportReq, HttpServletResponse response) {
        // 创建临时目录
        String srcPath = FileOperateUtils.generateTemporaryDirectoryName();
        String dstPath = srcPath + File.separator
                + ExportTypeAndFileNameEnum.getFileNameByType(exportReq.getResourceType());
        try {
            // 获取导出处理器，处理导出任务
            ExportHandler exportHandler = ExportHandlerFactory.getExportHandler(exportReq.getResourceType());
            exportHandler.handleExportTask(exportReq.getIds(), srcPath);
            // 压缩所有文件
            FileOperateUtils.zip(srcPath, dstPath);
            // 清空response
            response.reset();
            // 返回文件流
            try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
                File file = new File(dstPath);
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", String.format("attachment;filename=%s",
                        ExportTypeAndFileNameEnum.getFileNameByType(exportReq.getResourceType())));
                toClient.write(FileUtils.readFileToByteArray(file));
                toClient.flush();
            } catch (FileNotFoundException fnfe) {
                LOGGER.error("{} does not exist.",
                        ExportTypeAndFileNameEnum.getFileNameByType(exportReq.getResourceType()));
            } catch (IOException ex) {
                LOGGER.error("Export {} jobs exception ", exportReq.getResourceType(), ex);
            }
        } finally {
            // 删除临时文件夹
            File srcDirectory = new File(srcPath);
            FileOperateUtils.deleteDirectory(srcDirectory);
        }
    }

    @Override
    public ImportHistoryResp getImportHistory(ImportHistoryReq importHistoryReq) {
        Integer pageSize = importHistoryReq.getPageSize();
        Integer pageNum = importHistoryReq.getPageIndex();
        Specification<ImportHistoryEntity> specification = queryHistorySpecification(importHistoryReq);
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "lastModifiedDate"));
        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(pageNum > 0 ? (pageNum - 1) : pageNum, pageSize, sort);
        Page<ImportHistoryEntity> all = importHistoryRepository.findAll(specification, pageable);
        if (all.getTotalPages() < pageNum && all.getTotalPages() > 0) {
            pageable = PageRequest.of(all.getTotalPages() - 1, pageSize, sort);
            all = importHistoryRepository.findAll(specification, pageable);
        }
        ImportHistoryResp importHistoryResp = new ImportHistoryResp();
        List<History> histories = new ArrayList<>();
        all.getContent().forEach(historyEntity -> {
            History history = new History();
            BeanUtils.copyProperties(historyEntity, history);
            histories.add(history);
        });
        importHistoryResp.setHistories(histories);
        importHistoryResp.setTotal(all.getTotalElements());
        return importHistoryResp;
    }

    @Override
    public ImportDetailResp getImportDetail(String id) {
        long total = importHistoryRepository.findById(id)
                .orElseThrow(() -> new DataToolRuntimeException(ExceptionCode.DATATOOL_HISTORY_NOT_EXIST)).getTotal();
        ImportDetailResp detailResp = new ImportDetailResp();
        detailResp.setTotal(total);
        List<ImportDetailEntity> detailEntities = importHelper.findAllByImportHistoryId(id);
        DetailNumber detailNumber = importHelper.detailNumber(id);
        detailResp.setIgnored(detailNumber.getIgnored());
        detailResp.setSuccess(detailNumber.getSuccess());
        detailResp.setFailure(detailNumber.getFailed());
        List<Detail> details = new ArrayList<>();
        for (ImportDetailEntity importDetailEntity : detailEntities) {
            Detail detail = new Detail();
            detail.setName(importDetailEntity.getFileName());
            detail.setStatus(I18nUtils.getMessage(importDetailEntity.getStatus()));
            if (importDetailEntity.getErrorCode() != null) {
                detail.setMessage(I18nUtils.getMessage(importDetailEntity.getErrorCode(),
                        CommonUtils.transformProperty(importDetailEntity.getParams()).split(";")));
            }
            details.add(detail);
        }
        detailResp.setDetails(details);
        return detailResp;
    }

    private Specification<ImportHistoryEntity> queryHistorySpecification(ImportHistoryReq importHistoryReq) {
        return (Root<ImportHistoryEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteria) -> {
            criteriaQuery.distinct(true);
            Predicate preAnd = criteria
                    .and(getHistoryPredicates(root, importHistoryReq, criteria).toArray(new Predicate[0]));
            criteriaQuery.where(preAnd);
            return preAnd;
        };
    }

    public static List<Predicate> getHistoryPredicates(Root<ImportHistoryEntity> root,
            ImportHistoryReq importHistoryReq, CriteriaBuilder criteria) {
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(importHistoryReq.getResourceName())) {
            Predicate predicate = criteria.like(criteria.lower(root.get("resourceName")), "%"
                    + CommonUtil.parameterEscape(importHistoryReq.getResourceName()).toLowerCase(Locale.ROOT) + "%");
            predicates.add(predicate);
        }
        if (!StringUtils.isEmpty(importHistoryReq.getResourceImportStatus())) {
            Predicate predicate = criteria.equal(root.get("status"), importHistoryReq.getResourceImportStatus());
            predicates.add(predicate);
        }
        if (!StringUtils.isEmpty(importHistoryReq.getResourceImportMode())) {
            Predicate predicate = criteria.equal(root.get("importMode"), importHistoryReq.getResourceImportMode());
            predicates.add(predicate);
        }
        if (!StringUtils.isEmpty(importHistoryReq.getResourceOrigin())) {
            Predicate predicate = criteria.equal(root.get("resourceOrigin"), importHistoryReq.getResourceOrigin());
            predicates.add(predicate);
        }

        if (!StringUtils.isEmpty(importHistoryReq.getResourceType())) {
            Predicate predicate = criteria.equal(root.get("resourceType"), importHistoryReq.getResourceType());
            predicates.add(predicate);
        }
        if (importHistoryReq.getResourceImportPeriod() != null
                && importHistoryReq.getResourceImportPeriod().size() == 2) {
            String startTime = importHistoryReq.getResourceImportPeriod().get(0);
            String endTime = importHistoryReq.getResourceImportPeriod().get(1);
            predicates.add(criteria.greaterThanOrEqualTo(root.get("createdDate"), TimeUtil.getDate(startTime)));
            predicates.add(criteria.lessThanOrEqualTo(root.get("createdDate"), TimeUtil.getDate(endTime)));
        }
        return predicates;
    }
}