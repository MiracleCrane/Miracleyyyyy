/*
 * 文 件 名:  AssetOverviewServiceImpl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.impl;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.application.service.AssetOverviewService;
import com.huawei.smartcampus.datatool.monitor.application.service.ExportTask;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobAmountResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobAmountVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobStateAmountResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobStateAmountVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobStateTypeAmountVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.OverviewResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.SchemaData;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.TableAmountData;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.TableAmountItem;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.UsageData;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.AssetOVGateWay;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BusinessDbInfoGateway;
import com.huawei.smartcampus.datatool.monitor.domain.overview.AssetOriginEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.JobStatisticsType;
import com.huawei.smartcampus.datatool.monitor.domain.overview.ModelLayerEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.TableDetailVo;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task.AssetListInfoTask;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task.BatchJobDetailTask;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task.BatchScriptDetailTask;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task.BatchScriptJobOverviewTask;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task.StreamJobDetailTask;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task.TableDetailTask;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task.TableOverviewTask;
import com.huawei.smartcampus.datatool.utils.FileUtil;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

/**
 * 资产概览-作业资产-实现类
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Service
public class AssetOverviewServiceImpl implements AssetOverviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetOverviewServiceImpl.class);

    private final ExecutorService executorService = new ThreadPoolExecutor(10, 64, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10));

    private static final String EXPORT_FILE_NAME = "DataAssetsList.xlsx";

    private static final long TASK_WAITING_TIME = 60000L;

    @Autowired
    private AssetOVGateWay assetOVGW;

    @Autowired
    private BusinessDbInfoGateway businessDbInfoGateway;

    @Override
    public JobAmountResponse queryJobAmount() {
        JobAmountResponse response = new JobAmountResponse();
        response.setMaximumStreamJobNum(assetOVGW.getMaxJobNum(JobStatisticsType.STREAM_JOB));
        response.setMaximumBatchJobNum(assetOVGW.getMaxJobNum(JobStatisticsType.BATCH_JOB));
        response.setMaximumBatchScriptNum(assetOVGW.getMaxJobNum(JobStatisticsType.BATCH_SCRIPT));
        int batchJobNum = assetOVGW.getJobNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.ALL);
        int streamJobNum = assetOVGW.getJobNum(JobStatisticsType.STREAM_JOB, GroupStatisticsEnum.ALL);
        response.setAll(new JobAmountVo(batchJobNum, streamJobNum,
                assetOVGW.getJobNum(JobStatisticsType.BATCH_SCRIPT, GroupStatisticsEnum.ALL)));
        response.setBaseline(
                new JobAmountVo(assetOVGW.getJobNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.BASELINE),
                        assetOVGW.getJobNum(JobStatisticsType.STREAM_JOB, GroupStatisticsEnum.BASELINE),
                        assetOVGW.getJobNum(JobStatisticsType.BATCH_SCRIPT, GroupStatisticsEnum.BASELINE)));
        response.setCustom(new JobAmountVo(assetOVGW.getJobNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.CUSTOM),
                assetOVGW.getJobNum(JobStatisticsType.STREAM_JOB, GroupStatisticsEnum.CUSTOM),
                assetOVGW.getJobNum(JobStatisticsType.BATCH_SCRIPT, GroupStatisticsEnum.CUSTOM)));
        response.setSummary(batchJobNum + streamJobNum);
        return response;
    }

    @Override
    public JobStateAmountResponse queryJobState() {
        JobStateAmountResponse response = new JobStateAmountResponse();
        int allUsedBatchJob = assetOVGW.getJobStateNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.ALL, true);
        int allUnusedBatchJob = assetOVGW.getJobStateNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.ALL, false);
        int allUsedStreamJob = assetOVGW.getJobStateNum(JobStatisticsType.STREAM_JOB, GroupStatisticsEnum.ALL, true);
        int allUnusedStreamJob = assetOVGW.getJobStateNum(JobStatisticsType.STREAM_JOB, GroupStatisticsEnum.ALL, false);
        response.setAll(new JobStateTypeAmountVo(
                new JobStateAmountVo(allUsedBatchJob + allUnusedBatchJob, allUsedBatchJob, allUnusedBatchJob),
                new JobStateAmountVo(allUsedStreamJob + allUnusedStreamJob, allUsedStreamJob, allUnusedStreamJob)));
        int baselineUsedBatchJob = assetOVGW.getJobStateNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.BASELINE,
                true);
        int baselineUnusedBatchJob = assetOVGW.getJobStateNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.BASELINE,
                false);
        int baselineUsedStreamJob = assetOVGW.getJobStateNum(JobStatisticsType.STREAM_JOB, GroupStatisticsEnum.BASELINE,
                true);
        int baselineUnusedStreamJob = assetOVGW.getJobStateNum(JobStatisticsType.STREAM_JOB,
                GroupStatisticsEnum.BASELINE, false);
        response.setBaseline(new JobStateTypeAmountVo(
                new JobStateAmountVo(baselineUsedBatchJob + baselineUnusedBatchJob, baselineUsedBatchJob,
                        baselineUnusedBatchJob),
                new JobStateAmountVo(baselineUsedStreamJob + baselineUnusedStreamJob, baselineUsedStreamJob,
                        baselineUnusedStreamJob)));
        int customUsedBatchJob = assetOVGW.getJobStateNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.CUSTOM,
                true);
        int customUnusedBatchJob = assetOVGW.getJobStateNum(JobStatisticsType.BATCH_JOB, GroupStatisticsEnum.CUSTOM,
                false);
        int customUsedStreamJob = assetOVGW.getJobStateNum(JobStatisticsType.STREAM_JOB, GroupStatisticsEnum.CUSTOM,
                true);
        int customUnusedStreamJob = assetOVGW.getJobStateNum(JobStatisticsType.STREAM_JOB, GroupStatisticsEnum.CUSTOM,
                false);
        response.setCustom(new JobStateTypeAmountVo(
                new JobStateAmountVo(customUsedBatchJob + customUnusedBatchJob, customUsedBatchJob,
                        customUnusedBatchJob),
                new JobStateAmountVo(customUsedStreamJob + customUnusedStreamJob, customUsedStreamJob,
                        customUnusedStreamJob)));
        response.setSummary(
                new JobStateAmountVo(allUsedBatchJob + allUnusedBatchJob + allUsedStreamJob + allUnusedStreamJob,
                        allUsedBatchJob + allUsedStreamJob, allUnusedBatchJob + allUnusedStreamJob));
        return response;
    }

    @Override
    public OverviewResponse<TableAmountItem, TableAmountData<TableAmountItem>> queryTableAmount() {
        List<TableDetailVo> details = getTableDetail();
        details.addAll(getAllTableSchema());
        // layer、schema、table
        Map<ModelLayerEnum, Map<String, Set<Long>>> customMap = new HashMap<>();
        Map<ModelLayerEnum, Map<String, Set<Long>>> baseLineMap = new HashMap<>();
        Set<Long> tableIdSet = new HashSet<>();
        Set<String> schemaSet = new HashSet<>();
        for (TableDetailVo detail : details) {
            schemaSet.add(detail.getSchema());
            if (tableIdSet.contains(detail.getTableId())) {
                continue;
            } else if (detail.getTableId() != null) {
                tableIdSet.add(detail.getTableId());
            }
            // 这里不会有问题，不然就是代码有问题
            AssetOriginEnum source = AssetOriginEnum.valueOf(detail.getSource().toUpperCase(Locale.ROOT));
            ModelLayerEnum layer = ModelLayerEnum.valueOf(detail.getModelLayering().toUpperCase(Locale.ROOT));
            switch (source) {
                case BASELINE:
                    processLayer(layer, baseLineMap, detail);
                    break;
                case CUSTOM:
                    processLayer(layer, customMap, detail);
                    break;
                default:
                    LOGGER.error("The database detail table contains dirty data.");
            }
        }
        OverviewResponse<TableAmountItem, TableAmountData<TableAmountItem>> response = new OverviewResponse<>();
        // summary没有detail
        response.setSummary(new TableAmountItem(schemaSet.size(), tableIdSet.size()));
        response.setAll(getTableAmount(getAllTableAmount(baseLineMap, customMap)));
        response.setBaseline(getTableAmount(baseLineMap));
        response.setCustom(getTableAmount(customMap));
        return response;
    }

    @Override
    public void exportList(HttpServletResponse response) {
        List<ExportTask<?>> taskList = new ArrayList<>();
        taskList.add(new AssetListInfoTask());
        taskList.add(new TableOverviewTask());
        taskList.add(new TableDetailTask());
        taskList.add(new BatchScriptJobOverviewTask());
        taskList.add(new BatchJobDetailTask());
        taskList.add(new BatchScriptDetailTask());
        taskList.add(new StreamJobDetailTask());
        List<Future<?>> futureList = new ArrayList<>();
        List<Object> resultList = new ArrayList<>();
        try {
            for (ExportTask<?> task : taskList) {
                Future<?> future = executorService.submit(task);
                futureList.add(future);
            }
            queryTaskResultLoop(futureList, resultList);
        } catch (DataToolRuntimeException e) {
            throw e;
        } catch (Throwable e) {
            LOGGER.error("execute task failed", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).fillSheetData(workbook, resultList.get(i));
        }
        doExport(workbook, response);
    }

    private Map<ModelLayerEnum, Map<String, Set<Long>>> getAllTableAmount(
            Map<ModelLayerEnum, Map<String, Set<Long>>> baselineMap,
            Map<ModelLayerEnum, Map<String, Set<Long>>> customMap) {
        Map<ModelLayerEnum, Map<String, Set<Long>>> mergedMap = new HashMap<>();
        // 表明细中缺少某个层级数据导致对应分层数据为null的情况，分层遍历的时候做处理
        for (ModelLayerEnum layer : ModelLayerEnum.values()) {
            Map<String, Set<Long>> baselineLayer = baselineMap.computeIfAbsent(layer, k -> new HashMap<>());
            Map<String, Set<Long>> mergedLayer = new HashMap<>(baselineLayer);
            Map<String, Set<Long>> customLayer = customMap.computeIfAbsent(layer, k -> new HashMap<>());
            for (Map.Entry<String, Set<Long>> entry : customLayer.entrySet()) {
                String key = entry.getKey();
                if (baselineLayer.containsKey(key)) {
                    Set<Long> baselineSet = baselineLayer.computeIfAbsent(key, k -> new HashSet<>());
                    Set<Long> mergedSet = new HashSet<>(baselineSet);
                    mergedSet.addAll(customLayer.get(key));
                    mergedLayer.put(key, mergedSet);
                } else {
                    mergedLayer.put(key, customLayer.get(key));
                }
            }
            mergedMap.put(layer, mergedLayer);
        }
        return mergedMap;
    }

    private TableAmountData<TableAmountItem> getTableAmount(Map<ModelLayerEnum, Map<String, Set<Long>>> baseLineMap) {
        TableAmountData<TableAmountItem> tableAmountData = new TableAmountData<>();
        tableAmountData.setDm(getLayerAmount(baseLineMap.get(ModelLayerEnum.DM)));
        tableAmountData.setDwi(getLayerAmount(baseLineMap.get(ModelLayerEnum.DWI)));
        tableAmountData.setDwr(getLayerAmount(baseLineMap.get(ModelLayerEnum.DWR)));
        return tableAmountData;
    }

    private TableAmountItem getLayerAmount(Map<String, Set<Long>> layerMap) {
        TableAmountItem layerAmount = new TableAmountItem();
        // 记录当前层级下表的总数
        int tableTotalNum = 0;
        List<SchemaData> schemaDetails = new ArrayList<>();
        // details中每个记录每个schema对应的表数量
        for (Map.Entry<String, Set<Long>> entry : layerMap.entrySet()) {
            String schema = entry.getKey();
            SchemaData schemaData = new SchemaData();
            schemaData.setSchemaName(schema);
            schemaData.setTableNum(layerMap.get(schema).size());
            schemaDetails.add(schemaData);
            tableTotalNum = tableTotalNum + schemaData.getTableNum();
        }
        layerAmount.setDetail(schemaDetails);
        layerAmount.setSchemaNum(layerMap.size());
        layerAmount.setTableNum(tableTotalNum);
        return layerAmount;
    }

    private void processLayer(ModelLayerEnum layer, Map<ModelLayerEnum, Map<String, Set<Long>>> layerMap,
            TableDetailVo detail) {
        Map<String, Set<Long>> schemaMap = layerMap.computeIfAbsent(layer, k -> new HashMap<>());
        Set<Long> tableSet = schemaMap.computeIfAbsent(detail.getSchema(), k -> new HashSet<>());
        if (detail.getTableId() != null) {
            tableSet.add(detail.getTableId());
        }
    }

    @Override
    public OverviewResponse<UsageData, TableAmountData<UsageData>> queryTableState() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start to process table detail.");
        }
        List<TableDetailVo> details = getTableDetail();
        businessDbInfoGateway.processTableState(details);
        // 这里必须都初始化，不然响应缺少layer
        OverviewResponse<UsageData, TableAmountData<UsageData>> response = new OverviewResponse<UsageData, TableAmountData<UsageData>>()
                .setSummary(new UsageData())
                .setBaseline(new TableAmountData<UsageData>().setDwr(new UsageData()).setDwi(new UsageData())
                        .setDm(new UsageData()))
                .setCustom(new TableAmountData<UsageData>().setDwr(new UsageData()).setDwi(new UsageData())
                        .setDm(new UsageData()))
                .setAll(new TableAmountData<UsageData>().setDwr(new UsageData()).setDwi(new UsageData())
                        .setDm(new UsageData()));
        for (TableDetailVo detail : details) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Table state detail:{}", detail.toString());
            }
            AssetOriginEnum source = AssetOriginEnum.valueOf(detail.getSource().toUpperCase(Locale.ROOT));
            ModelLayerEnum layer = ModelLayerEnum.valueOf(detail.getModelLayering().toUpperCase(Locale.ROOT));
            boolean isUsed = detail.getUsed().getBooleanValue();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Table usage state {},table real state {}", isUsed, detail.getUsed().value());
            }
            // 取到表数据对应对应的all中的layer和所属的基线或项目的的layer，
            UsageData layerOfAll = getUsageDataForLayer(response.getAll(), layer);
            UsageData layerOfSource = getUsageDataForSourceAndLayer(response, source, layer);
            // summary先++
            response.getSummary().incrementTotal();
            layerOfAll.incrementTotal();
            layerOfSource.incrementTotal();
            if (isUsed) {
                response.getSummary().incrementUsed();
                layerOfAll.incrementUsed();
                layerOfSource.incrementUsed();
            } else {
                response.getSummary().incrementUnused();
                layerOfAll.incrementUnused();
                layerOfSource.incrementUnused();
            }
        }
        return response;
    }

    private UsageData getUsageDataForLayer(TableAmountData<UsageData> tableAmountData, ModelLayerEnum layer) {
        UsageData usageData = null;
        switch (layer) {
            case DM:
                usageData = tableAmountData.getDm();
                break;
            case DWI:
                usageData = tableAmountData.getDwi();
                break;
            case DWR:
                usageData = tableAmountData.getDwr();
                break;
            default:
                break;
        }
        return usageData;
    }

    private UsageData getUsageDataForSourceAndLayer(OverviewResponse<UsageData, TableAmountData<UsageData>> response,
            AssetOriginEnum source, ModelLayerEnum layer) {
        TableAmountData<UsageData> tableAmountData = null;
        switch (source) {
            case BASELINE:
                tableAmountData = response.getBaseline();
                break;
            case CUSTOM:
                tableAmountData = response.getCustom();
                break;
            default:
                break;
        }
        return getUsageDataForLayer(tableAmountData, layer);
    }

    private List<TableDetailVo> getTableDetail() {
        List<TableDetailVo> details = businessDbInfoGateway.getTableDetail();
        // 表明细中同一张表除了字段的信息外其他用不到，统计时候只需要表数据，每个表保留一条就可以了
        // 去重的时候要保留分区表字段，因为非分区表的isPartKey也是false，所以保留的时候优先保留相同tableId，isPartKey为true的
        return details.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(TableDetailVo::getTableId, Function.identity(),
                                (detail1, detail2) -> detail1.getPartKey() ? detail1 : detail2, LinkedHashMap::new),
                        map -> new ArrayList<>(map.values())));
    }

    private List<TableDetailVo> getAllTableSchema() {
        return businessDbInfoGateway.getAllTableSchema();
    }

    private void queryTaskResultLoop(List<Future<?>> futureList, List<Object> resultList) throws Throwable {
        long startTime = System.currentTimeMillis();
        boolean isTimeout = false;
        long currentTime;
        // 剩余等待时间
        long leftWaitingTime;
        for (Future<?> future : futureList) {
            currentTime = System.currentTimeMillis();
            try {
                leftWaitingTime = TASK_WAITING_TIME - (currentTime - startTime);
                // 所有任务总计等待60s
                if (leftWaitingTime <= 0) {
                    throw new TimeoutException();
                }
                resultList.add(future.get(leftWaitingTime, TimeUnit.MILLISECONDS));
            } catch (ExecutionException ex) {
                throw ex.getCause();
            } catch (TimeoutException ex) {
                if (!isTimeout) {
                    // 第一次任务执行超时，打印日志，避免多次打印重复日志
                    LOGGER.error("execute task timeout!", ex);
                    isTimeout = true;
                }
                // 超时后，仍需要尝试终止后续的任务
                future.cancel(true);
            } catch (Exception ex) {
                LOGGER.error("execute task failed!", ex);
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
            }
        }
        if (isTimeout) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
    }

    private void doExport(XSSFWorkbook workbook, HttpServletResponse response) {
        // 创建临时目录名称
        String tempPath = System.getProperty("java.io.tmpdir") + File.separator
                + UUID.randomUUID().toString().substring(0, 5);
        try {
            Files.deleteIfExists(Paths.get(tempPath));
        } catch (IOException e) {
            LOGGER.error("Delete file {} failed.", tempPath, e);
        }
        File tempDir = new File(tempPath);
        // 创建临时目录
        boolean isCreateDirSuccess = tempDir.mkdir();
        if (!isCreateDirSuccess) {
            LOGGER.error("create export file dir failed.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
        String excelPath = tempPath + File.separator + EXPORT_FILE_NAME;
        try (OutputStream fOut = new FileOutputStream(excelPath);
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            workbook.write(fOut);
            File file = new File(excelPath);
            response.setHeader("Content-Disposition", String.format("attachment;filename=%s", EXPORT_FILE_NAME));
            toClient.write(FileUtils.readFileToByteArray(file));
            toClient.flush();
        } catch (FileNotFoundException fe) {
            LOGGER.error("{} does not exist.", EXPORT_FILE_NAME);
        } catch (IOException ex) {
            LOGGER.error("Export {} jobs exception", EXPORT_FILE_NAME, ex);
        } finally {
            // 删除临时目录
            FileUtil.deleteDirectory(tempPath);
        }
    }
}