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
import com.github.pagehelper.PageInfo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
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

        Table table = new Table(3);
        table.addCell("ID");
        table.addCell("姓名");
        table.addCell("邮箱");

        for (User user : users) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getName());
            table.addCell(user.getEmail() == null ? "" : user.getEmail());
        }

        document.add(table);
        document.close();
    }

    // 上传 CSV 文件导入用户数据
    @PostMapping("/api/users/import/csv")
    public String importCSV(@RequestParam("file") MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> rows = csvReader.readAll();

            // 防止文件为空或只有表头
            if (rows.size() <= 1) {
                return "CSV内容为空或缺少数据行";
            }

            List<User> users = new ArrayList<>();
            for (int i = 1; i < rows.size(); i++) { // 从索引1开始跳过表头
                String[] data = rows.get(i);
                if (data.length < 2) {
                    System.err.println("跳过无效行：" + Arrays.toString(data));
                    continue; // 跳过非法行
                }

                String username = data[0].trim();
                String email = data[1].trim();

                if (username.isEmpty() || email.isEmpty()) {
                    System.err.println("跳过空值行：" + Arrays.toString(data));
                    continue;
                }

                User user = new User(null, username, email);
                users.add(user);
            }

            for (User user : users) {
                userService.addUser(user);
            }

            return "导入成功，导入用户数：" + users.size();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return "导入失败：" + e.getMessage();
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
    public User addUser(@RequestBody User user) {
    	logger.info("添加用户: {}", user);
        userService.addUser(user);
        return user;
    }

    // 修改用户
    @ResponseBody
    @PutMapping("/api/users/{id}")
    public User updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
    	logger.info("更新用户: {}", user);
        user.setId(id);
        userService.updateUser(user);
        return user;
    }

    // 删除用户
    @ResponseBody
    @DeleteMapping("/api/users/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
    	logger.warn("删除用户 ID: {}", id);
        userService.deleteUser(id);
        return "success";
    }
}


