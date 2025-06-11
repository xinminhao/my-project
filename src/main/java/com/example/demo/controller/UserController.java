package com.example.demo.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.MessageUtil;
import com.github.pagehelper.PageInfo;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
    	logger.info("访问首页");
        return "index";
    }

    @ResponseBody
    @GetMapping("/api/users/export/excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<User> users = userService.findUsers(1, Integer.MAX_VALUE, null, null).getList();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("用户列表");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("姓名");
        header.createCell(2).setCellValue("邮箱");

        int rowIdx = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName());
            row.createCell(2).setCellValue(user.getEmail());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/api/users/export/pdf")
    public void exportPdf(HttpServletResponse response) throws IOException {
        List<User> users = userService.findUsers(1, Integer.MAX_VALUE, null, null).getList();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=users.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 加载系统字体（如微软雅黑）
        String fontPath = "C:/Windows/Fonts/msyh.ttc"; // Windows 系统示例
        PdfFont font = PdfFontFactory.createFont(fontPath + ",0", PdfEncodings.IDENTITY_H, pdf);

        // 添加标题
        Paragraph title = new Paragraph("用户信息列表")
                .setFont(font)
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);

        float[] columnWidths = {60F, 150F, 250F};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        // 表头
        table.addHeaderCell(createHeaderCell("ID", font));
        table.addHeaderCell(createHeaderCell("姓名", font));
        table.addHeaderCell(createHeaderCell("邮箱", font));

        // 内容行
        for (User user : users) {
            table.addCell(createBodyCell(String.valueOf(user.getId()), font));
            table.addCell(createBodyCell(user.getName(), font));
            table.addCell(createBodyCell(user.getEmail() == null ? "" : user.getEmail(), font));
        }

        document.add(table);
        document.close();
    }

    private Cell createHeaderCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5);
    }

    private Cell createBodyCell(String text, PdfFont font) {
        return new Cell()
                .add(new Paragraph(text).setFont(font))
                .setTextAlignment(TextAlignment.LEFT)
                .setPadding(5);
    }



    // 上传 CSV 文件导入用户数据
    @PostMapping("/api/users/import/csv")
    public String importCSV(@RequestParam("file") MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> rows = csvReader.readAll();
            if (rows.size() <= 1) {
                return MessageUtil.get("user.import.fail", "CSV内容为空或缺少数据行");
            }

            List<User> users = new ArrayList<>();
            for (int i = 1; i < rows.size(); i++) {
                String[] data = rows.get(i);
                if (data.length < 2 || data[0].trim().isEmpty() || data[1].trim().isEmpty()) {
                    continue;
                }
                users.add(new User(null, data[0].trim(), data[1].trim()));
            }

            for (User user : users) {
                userService.addUser(user);
            }

            return MessageUtil.get("user.import.success", users.size());
        } catch (Exception e) {
            return MessageUtil.get("user.import.fail", e.getMessage());
        }
    }
    
    @ResponseBody
    @GetMapping("/api/users")
    public PageInfo<User> getUsers(@RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email) {
    	logger.info("查询用户列表: page={}, size={}, name={}, email={}", page, size, name, email);
        return userService.findUsers(page, size, name, email);
    }
    
    // 新增用户
    @ResponseBody
    @PostMapping("/api/users")
    public String addUser(@RequestBody User user) {
        logger.info("添加用户: {}", user);
        userService.addUser(user);
        return MessageUtil.get("user.add.success", user.getName());
    }

    @ResponseBody
    @PutMapping("/api/users/{id}")
    public String updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
        logger.info("更新用户: {}", user);
        user.setId(id);
        userService.updateUser(user);
        return MessageUtil.get("user.update.success", user.getName());
    }

    // 删除用户
    @ResponseBody
    @DeleteMapping("/api/users/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        logger.warn("删除用户 ID: {}", id);
        userService.deleteUser(id);
        return MessageUtil.get("user.delete.success", id);
    }
    
    @PostMapping("/api/users/batch-delete")
    public String deleteUsers(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return MessageUtil.get("user.delete.batch.empty");
        }
        userService.deleteUsersByIds(ids);
        return MessageUtil.get("user.delete.batch.success");
    }

}


