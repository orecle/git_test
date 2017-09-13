package com.dzd.phonebook.util;

import com.dzd.phonebook.entity.Customer;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.page.MobileCheckUtil;
import com.dzd.phonebook.service.CustomerService;
import com.dzd.phonebook.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作文件辅助类
 *
 * @author CHENCHAO
 * @date 2017-3-25 14:43:00
 */
public class FileUploadUtil {
    public static final Logger log = LoggerFactory.getLogger(FileUploadUtil.class);
    public static final String PHONE_PATH = "/fileUpload/";
    private static final String readLineText = "qwxtReadLine";

    /**
     * 获取电话分类的个数
     *
     * @param multipartFiles
     * @return
     */
    public static Map<String, Object> getSmsPhoneCategoryMap(MultipartFile[] multipartFiles,SysUser user,
                                                             CustomerService customerService,SysUserService sysUserService,Object temp) {
        // 1.获取号码集合
        List<Customer> phoneList = new ArrayList<Customer>();
        String fileName = "";
        try {
            /** 创建临时文件 **/
            File tempFile = File.createTempFile("tempFile", null);
            multipartFiles[0].transferTo(tempFile);

            /** 获取文件的后缀 **/
            fileName = multipartFiles[0].getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));

           // Excel .xlsx 调用此方法
           /*if (suffix.equals(".xlsx")) {
                phoneList = readDataByExcelXLSX(tempFile,user,sysUserService);// 获取Excel中的电话号码
                // Excel .xls 调用此方法
            } else if (suffix.equals(".xls")) {
                phoneList = readDataByExcelXLS(tempFile);// 获取Excel中的电话号码
            }*/

            phoneList = readDataByExcel(tempFile, user, sysUserService,temp);// 获取Excel中的电话号码

        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》获取电话号码集合发生异常：" + e.getMessage());
        }


        // 2. 根据号码，对号码进行分类
        Map<String, Object> smsMap = new HashMap<String, Object>();
        try {
            // 分出重复号码、无效号码、有效号码
            smsMap = MobileCheckUtil.mobileAssort(phoneList,customerService);

           // map.put("fileName", PHONE_PATH + fileName);
           // map.put("smsMap", smsMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("号码进行分类发生错误：" + e.getMessage());
        }

        return smsMap;
    }


    private static List<String> getValidList(String operators, Map<String, List<String>> smsMap) {
        List<String> validList = new ArrayList<String>();
        if (StringUtils.isEmpty(operators)) {
            validList = smsMap.get(Define.PHONEKEY.VALID);
        } else {
            List<String> beforvalidList = smsMap.get(Define.PHONEKEY.VALID);

            List<String> mobiles = new ArrayList<String>();// 移动
            List<String> unicoms = new ArrayList<String>();// 联通
            List<String> telecoms = new ArrayList<String>();// 电信
            List<String> unknown = new ArrayList<String>();// 未知

            // 0：联通，1：移动，2：电信
            for (String phone : beforvalidList) {
                if (0 == DistinguishOperator.getSupplier(phone)) {
                    unicoms.add(phone);
                } else if (1 == DistinguishOperator.getSupplier(phone)) {
                    mobiles.add(phone);
                } else if (2 == DistinguishOperator.getSupplier(phone)) {
                    telecoms.add(phone);
                } else if (-1 == DistinguishOperator.getSupplier(phone)) {
                    unknown.add(phone);
                }
            }

            String[] split = operators.split(",");
            for (int i = 0; i < split.length; i++) {
                if ("0".equals(split[i])) {
                    validList.addAll(unicoms);
                } else if ("1".equals(split[i])) {
                    validList.addAll(mobiles);
                } else if ("2".equals(split[i])) {
                    validList.addAll(telecoms);
                }
            }

        }
        return validList;
    }

    /**
     * 根据TXT文本获取电话号码
     *
     * @param tempFile
     * @return
     */
    public static List<String> readDataByTxt(File tempFile) {
        log.info("-----------------》文件名称：" + tempFile.getName());
        List<String> phoneList = new ArrayList<String>();// 号码集合
        try {
            String code = resolveCode(tempFile);// 获取文本文件的编码格式
            InputStreamReader reader = new InputStreamReader(new FileInputStream(tempFile), code);
            BufferedReader br = new BufferedReader(reader);
            String s = br.readLine();
            while (s != null) {
                if (!s.equals("")) {// 本行数据不为空，进行添加
                    if (!readLineText.equals(s)) {
                        phoneList.add(s.trim());
                    }
                    s = br.readLine();
                } else {// 遇到空数据，重新赋值读取下一行
                    s = readLineText;
                }
            }
            br.close();
            // 判断UTF-8格式第一行会生成特殊符号并去除
            if (code.equals("UTF-8")) {
                String phone = phoneList.get(0);
                System.out.print(phone + ",length:" + phone.length());
                if (phone.length() > 11) {
                    phone = phone.substring(1, phone.length());
                    phoneList.remove(0);
                    phoneList.add(0, phone);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》根据TXT获取号码发生异常：" + e.getMessage());
        }
        return phoneList;
    }


    /**
     * 调用此方法
     *
     * @param tempFile
     * @return
     */
    public static List<Customer> readDataByExcel(File tempFile, SysUser user, SysUserService sysUserService,Object temp) {
        log.info("-----------------》文件名称：" + tempFile.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d= new Date();
    //    long logn1 = System.currentTimeMillis();
        List<Customer> list = new ArrayList<Customer>();
        try {
            InputStream is = new FileInputStream(tempFile);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            // Read the Sheet
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                // Read the Row
                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        Customer customer = new Customer();
                        XSSFCell cname = xssfRow.getCell(0);
                        XSSFCell telephone = xssfRow.getCell(1);
                        XSSFCell remark = xssfRow.getCell(2);
                        XSSFCell stage = xssfRow.getCell(3);
                        XSSFCell userid = xssfRow.getCell(4);
                        XSSFCell source = xssfRow.getCell(5);

                        customer.setCname(getValue(cname));
                        customer.setTelephone(getValue(telephone));
                        customer.setRemark(getValue(remark));

                        if (stage != null) {
                            if (com.dzd.phonebook.util.StringUtils.isNotBlank(getValue(stage))) {
                                customer.setStage(Integer.parseInt(getValue(stage)));
                            } else {
                                customer.setStage(null);
                            }
                        } else {
                            customer.setStage(null);
                        }
                        if (user.getRole() == 3 && temp == null) {   //当前登录用户为业务员
                            customer.setUserid(user.getId());
                            customer.setAllocatedTime(Timestamp.valueOf(sdf.format(d)));
                        } else {
                            if (userid != null) {
                                if (com.dzd.phonebook.util.StringUtils.isNotBlank(getValue(userid)) && temp == null) {
                                    Integer id = sysUserService.queryIdByEmail(getValue(userid));
                                    customer.setUserid(id);
                                } else {
                                    customer.setUserid(null);
                                }
                            } else {
                                customer.setUserid(null);
                            }
                        }
                        customer.setIsFromCustomer(0); //系统用户
                        customer.setDeleted(0);
                        customer.setIsCheck(1);
                        customer.setSource(getValue(source));
                        list.add(customer);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("-----------------》发生异常：" + ex.getMessage());
        }

     //   long logn2 = System.currentTimeMillis();

       // System.out.println(logn2 - logn1);

        System.out.println("共读取数据的行数:" + list.size());
        return list;
    }

    @SuppressWarnings("static-access")
    public static String getValue(XSSFCell xssfRow) {
        if (xssfRow != null) {
            if (xssfRow.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                return new DecimalFormat("0").format(xssfRow.getNumericCellValue());
            } else if (xssfRow.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                return String.valueOf(xssfRow.getStringCellValue());
            } else if (xssfRow.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                //空白
                return xssfRow.getStringCellValue();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Excel后缀为.xlsx的版本调用此方法
     *
     * @param tempFile
     * @return
     */
    public static List<Customer> readDataByExcelXLSX(File tempFile,SysUser user,SysUserService sysUserService) {
        log.info("-----------------》文件名称：" + tempFile.getName());
       // List<String> phoneList = new ArrayList<String>();// 号码集合
        List<Customer> list=new ArrayList<Customer>();
        try {
            // 创建对Excel工作簿文件的引用
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(tempFile));
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();  //总行数
            for (int i = 1; i < rows; i++) {
                Customer customer=new Customer();
                XSSFRow row = sheet.getRow(i); //读取某一行数据
                if (row != null) {
                    int cols=row.getLastCellNum();   //获取行中所有列数据
                    for (int j = 0; j <cols; j++) {
                        XSSFCell cell = row.getCell(j);
                        String value="";
                        if(cell==null){
                           // System.out.print("--------空行---------");
                        }else{
                            if(j==0){
                                if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                                }else{
                                    value=cell.getStringCellValue();
                                }
                                customer.setCname(value);
                            }else if(j==1){
                                value = new DecimalFormat("0").format(cell.getNumericCellValue());
                                customer.setTelephone(value);
                            }else if(j==2){
                                value=cell.getStringCellValue();
                                customer.setRemark(value);
                            }else if(j==3){
                                value = new DecimalFormat("0").format(cell.getNumericCellValue());
                                customer.setStage(Integer.parseInt(value));
                            }else if(j==4){
                                if(user.getRole()==3){
                                    customer.setUserid(user.getId());
                                }else {
                                    value = cell.getStringCellValue();
                                    if(com.dzd.phonebook.util.StringUtils.isNotBlank(value)){
                                        Integer id=sysUserService.queryIdByEmail(value);
                                        customer.setUserid(id);
                                    }
                                }
                            } else if(j==5){
                                value=cell.getStringCellValue();
                                customer.setSource(value);
                            }else{
                            }
                            customer.setIsFromCustomer(0); //系统用户
                            customer.setDeleted(0);
                            customer.setIsCheck(1);
                          }
                        }
                    }
                list.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》根据Excel 2007以上版本 .xlsx获取号码发生异常：" + e.getMessage());
        }
        return list;
    }

    /**
     * 读取Excel后缀为.xls之下的版本调用此方法
     *
     * @param tempFile
     * @return
     */
    public static List<Customer> readDataByExcelXLS(File tempFile) {
        log.info("-----------------》文件名称：" + tempFile.getName());
      //  List<String> phoneList = new ArrayList<String>();// 号码集合
        List<Customer> phoneList = new ArrayList<Customer>();// 号码集合
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(tempFile));
            HSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                Customer customer=new Customer();
                HSSFRow row = sheet.getRow(i);
                if (row != null) {
                    int cells = row.getPhysicalNumberOfCells();
                    for (int j = 0; j < cells; j++) {
                        String value = "";
                        HSSFCell cell = row.getCell(j);
                        if(j==0){
                            if (cell != null) {
                                value=cell.getStringCellValue();
                            }
                            customer.setCname(value);
                        }else if(j==1){
                            if (cell != null) {
                                value = new DecimalFormat("0").format(cell.getNumericCellValue());
                            }
                            customer.setTelephone(value);
                        }else if(j==2){
                            if (cell != null) {
                                value=cell.getStringCellValue();
                            }
                            customer.setContent(value);
                        }else if(j==3){
                            if (cell != null) {
                                value=cell.getStringCellValue();
                            }
                            customer.setRemark(value);
                        }else if(j==4){
                            if (cell != null) {
                                value = new DecimalFormat("0").format(cell.getNumericCellValue());
                            }
                            customer.setStage(Integer.parseInt(value));
                        }else if(j==5){
                            if (cell != null) {
                                value = new DecimalFormat("0").format(cell.getNumericCellValue());
                            }
                            customer.setUserid(Integer.parseInt(value));
                        }else{
                            if (cell != null) {
                                value=cell.getStringCellValue();
                            }
                            customer.setSource(value);
                        }
                    }
                }
                phoneList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》根据Excel 低版本 .xls获取号码发生异常：" + e.getMessage());
        }
        return phoneList;
    }

    /**
     * 保存文件
     *
     * @param multipartFiles
     */
    public static String saveFile(MultipartFile[] multipartFiles) {
        try {

            String fileName = multipartFiles[0].getOriginalFilename();// 原来的文件名称
            String uploadFileName = fileName.substring(0, fileName.lastIndexOf("."));// 去除后缀的文件名
            String suffix = fileName.substring(fileName.lastIndexOf("."));// 后缀 .txt
            fileName = uploadFileName + "dzdqw" + RandomUtil.getRandomTenThousand() + suffix;// 拼接随机数生成的文件名
            return saveFileInfo(multipartFiles[0].getInputStream(),
                    new byte[(int) multipartFiles[0].getSize()], fileName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》保存文件发生异常：" + e.getMessage());
        }
        return "";
    }

    public static String saveFileInfo(InputStream is, byte[] bytes, String fileName)
            throws IOException, FileNotFoundException {
        // 1. 保存文件
        int read = 0;
        int i = 0;
        while ((read = is.read()) != -1) {
            bytes[i] = (byte) read;
            i++;
        }
        is.close();

        // 2.存放位置
        String catalinaHome = System.getProperty("catalina.home");
        String path = catalinaHome + PHONE_PATH;
        String txtPath = path + "/" + fileName;

        // 3. 判断文件夹是否存在，不存在则创建
        createDir(path);
        // createDir(txtPath);

        OutputStream os = new FileOutputStream(new File(txtPath));
        os.write(bytes);
        os.flush();
        os.close();
        return PHONE_PATH + fileName;
    }

    /**
     * 写入文件
     */
    public static void writerFile(List<String> phoneList, String fileName) {
        try {
            if (fileName != null && !fileName.equals("") && phoneList != null && phoneList.size() > 0) {

                // 创建目录
                String catalinaHome = System.getProperty("catalina.home");
                String path = catalinaHome + PHONE_PATH;
                createDir(path);

                String txtPath = path + fileName;

                File file = new File(txtPath);
                // 1.文件流
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));

                // 2.号码追加到StringBuffer
                StringBuffer sb = new StringBuffer();
                for (String element : phoneList) {
                    sb.append(element + "\n");
                }
                bw.write(sb.toString());
                bw.newLine();
                bw.close();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }

    /**
     * 删除单个文件
     *
     * @param filePath
     * @return
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {// 路径为文件且不为空则进行删除
            file.delete();// 文件删除
        }
    }

    /**
     * 创建目录
     *
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {// 判断目录是否存在
            return true;
        }
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            return true;
        } else {
            return false;
        }
    }

    /**
     * 下载(导出)号码
     *
     * @param projects
     * @return
     */
    public static List<Map<String, Object>> createExcelRecord(List<String> projects) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        for (int j = 0; j < projects.size(); j++) {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("id", projects.get(j));
            listmap.add(maps);
        }
        return listmap;
    }



    /**
     * 获取txt文件编码格式
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String resolveCode(File file) throws Exception {
        InputStream inputStream = new FileInputStream(file);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK
        if (head[0] == -1 && head[1] == -2)
            code = "UTF-16";
        else if (head[0] == -2 && head[1] == -1)
            code = "Unicode";
        else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
            code = "UTF-8";

        inputStream.close();

        System.out.println(code);
        return code;
    }


    public static String formatMobile(String mobile) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(mobile);
        mobile = m.replaceAll("").trim();
        return mobile;
    }
}
