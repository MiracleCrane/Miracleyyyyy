/*
 * 文 件 名:  FileUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.utils;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 文件获取工具类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class FileOperateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperateUtils.class);

    /**
     * 创建临时目录路径，带uuid
     *
     * @return 临时目录
     */
    public static String generateTemporaryDirectoryName() {
        return System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString().substring(0, 5);
    }

    /**
     * 创建目录
     *
     * @param dir 目录
     */
    public static void createTempDirectory(String dir) {
        File file = new File(dir);
        if (!file.exists() && !file.isDirectory()) {
            boolean isCreateDirSuccess = file.mkdir();
            LOGGER.info("isCreateDirSuccess: {}", isCreateDirSuccess);
        }
    }

    /**
     * 删除目录
     *
     * @param file 文件
     * @throws IOException IOException
     */
    public static void deleteDirectory(File file) {
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            LOGGER.error("deleteDirectory fail ", e);
        }
    }

    /**
     * 读取json文件
     *
     * @param fileName 文件名称
     * @return 读取结果
     */
    public static Optional<String> readJsonFromFile(String fileName) {
        try {
            return Optional.ofNullable(FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("readJsonFromFile fail ", e);
            return Optional.empty();
        }
    }

    /**
     * 写入json到文件
     *
     * @param json json
     * @param filePath 文件路径
     */
    public static void writeJsonToFile(String json, String filePath) {
        // json写入文件
        try {
            FileUtils.writeStringToFile(new File(filePath), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("writeJsonToFile fail ", e);
        }
    }

    /**
     * 解压
     *
     * @param originPath 压缩文件路径
     * @param dstPath 目标路径
     * @throws IOException IO异常
     */
    public static void unzip(String originPath, String dstPath) {
        Path zipFilePath = Paths.get(originPath);
        try (FileSystem fs = FileSystems.newFileSystem(zipFilePath, null)) {
            AtomicInteger entriesCount = new AtomicInteger(0);
            AtomicLong totalSize = new AtomicLong(0);
            Files.walkFileTree(fs.getPath(File.separator), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path target = Paths.get(dstPath, file.toString());
                    // 校验zip压缩包内文件个数是否过多
                    CheckUtils.checkFileCount(entriesCount);
                    // 校验zip压缩包内文件总大小是否过大
                    CheckUtils.checkSize(file, totalSize);
                    // 检查文件是否存在
                    CheckUtils.checkFileExist(target);
                    // 检查文件名是否合法
                    CheckUtils.checkZipChildFilePath(file);
                    Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    // 检查跨目录攻击
                    CheckUtils.checkDirectoryTraversal(dir.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOGGER.error("Failed to unzip {} to {}", zipFilePath, dstPath, e);
        }
    }

    /**
     * 压缩
     *
     * @param srcPath 源文件路径
     * @param dstPath 压缩文件路径
     */
    public static void zip(String srcPath, String dstPath) {
        try {
            Files.deleteIfExists(Paths.get(dstPath));
        } catch (IOException e) {
            LOGGER.error("Delete file {} failed", dstPath, e);
        }
        doZip(srcPath, dstPath);
    }

    /**
     * 压缩
     *
     * @param srcPath 源文件路径
     * @param dstPath 压缩文件目的路径
     */
    private static void doZip(String srcPath, String dstPath) {
        try (OutputStream fos = new FileOutputStream(dstPath);
                OutputStream bos = new BufferedOutputStream(fos);
                ArchiveOutputStream aos = new ZipArchiveOutputStream(bos)) {
            Path root = Paths.get(srcPath);
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                // 在访问到每个文件时调用
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    if (file.toString().equals(dstPath)) {
                        return FileVisitResult.CONTINUE;
                    }
                    try (InputStream input = new FileInputStream(file.toFile())) {
                        ArchiveEntry entry = new ZipArchiveEntry(file.toFile(), root.relativize(file).toString());
                        aos.putArchiveEntry(entry);
                        IOUtils.copy(input, aos);
                        aos.closeArchiveEntry();
                    }
                    return super.visitFile(file, attrs);
                }

                // 在访问任意目录前调用
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir.toString().equals(srcPath) || dir.toString().equals("")) {
                        return FileVisitResult.CONTINUE;
                    }
                    ArchiveEntry entry = new ZipArchiveEntry(dir.toFile(), root.relativize(dir).toString());
                    aos.putArchiveEntry(entry);
                    aos.closeArchiveEntry();
                    return super.preVisitDirectory(dir, attrs);
                }

                // 当前遍历文件访问失败
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    LOGGER.error("Failed to zip file {}", file, exc);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOGGER.error("Failed to zip {} to {}", srcPath, dstPath, e);
        }
    }

    /**
     * 导入的准备工作
     *
     * @param file 上传的文件
     * @param path 文件路径
     * @param zipFileTempName 文件名
     */
    public static void unzipFile(MultipartFile file, String path, String zipFileTempName) {
        try {
            // 创建临时文件夹
            createTempDirectory(path);
            // 保存到临时文件
            FileUtils.copyToFile(file.getInputStream(), new File(path + zipFileTempName));
            // 解压文件
            FileOperateUtils.unzip(path + zipFileTempName, path);
            // 删除压缩文件
            File temFile = new File(path + zipFileTempName);
            FileUtils.forceDelete(temFile);
        } catch (FileNotFoundException fnfe) {
            LOGGER.error("some files does not exist when prepare import file");
        } catch (IOException e) {
            LOGGER.error("exception happen when prepare import file", e);
        }
    }

    /**
     * 递归遍历文件夹下所有文件
     *
     * @param dir 文件夹路径
     * @param filePaths 文件路径集合
     * @return List<String> 数组
     */
    private static List<String> listFileDfs(File dir, List<String> filePaths) {
        File[] files = dir.listFiles();
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    listFileDfs(file, filePaths);
                } else {
                    filePaths.add(file.getCanonicalPath());
                }
            }
        } catch (IOException e) {
            LOGGER.error("exception happen when prepare import file", e);
        }
        return filePaths;
    }

    /**
     * 获取所有文件路径
     *
     * @param path 压缩临时文件路径
     * @return 所有文件路径列表
     */
    public static List<String> listFiles(String path) {
        List<String> filePaths = listFileDfs(new File(path), new ArrayList<>());
        if (filePaths.size() == 0) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ZIP_FILE_CANNOT_EMPTY);
        }
        return filePaths;
    }

    /**
     * 获取文件名称，不带后缀名
     *
     * @param file 文件
     * @return 文件名称
     */
    public static String getFileNameWithoutSuffix(MultipartFile file) {
        return file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
    }

    /**
     * 获取文件名称，不带后缀名
     *
     * @param filePath 文件绝对路径
     * @return 文件名称
     */
    public static String getFileNameWithoutSuffix(String filePath) {
        String filename = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        return filename.substring(0, filename.lastIndexOf("."));
    }

    /**
     * 获取文件后缀名，不包含.（点）
     *
     * @param file 文件
     * @return 文件名称
     */
    public static String getFileSuffix(MultipartFile file) {
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    /**
     * 获取文件后缀名，带点号
     *
     * @param filePath 文件绝对路径
     * @return 文件名称
     */
    public static String getFileSuffix(String filePath) {
        return filePath.substring(filePath.lastIndexOf("."));
    }
}