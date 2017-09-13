package com.dzd.phonebook.service;

import com.dzd.phonebook.entity.Customer;
import com.dzd.phonebook.util.StringUtils;
import com.github.pagehelper.Page;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IDEA Author: wangran Date: 2017/07/18 Time: 11:38
 */
public class CustomerBusiness
{
	private  static Logger logger = Logger.getLogger(CustomerBusiness.class );

	/**
	 * 导出客户管理信息
	 * @param response
	 * @param dataList
	 */
	public void export(HttpServletRequest request,HttpServletResponse response,
					   Page<Customer> dataList)
	{
		try
		{
			String sheetName = "客户管理信息";
			reportXls2(response, dataList,sheetName);// utils类需要用到的参数
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("导出失败");
			throw new RuntimeException("导出失败");
		}
	}

	/**
	 *
	 * 导出excel
	 */
	public static void reportXls2(HttpServletResponse response,
								  Page<Customer> dataList, String sheetName) throws Exception {

		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
		Sheet sheet=workbook.createSheet("客户管理列表");

		Font titleFont = workbook.createFont();
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setFont(titleFont);

		sheet.setDefaultColumnWidth(15);

		Row row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("序号");
		cell1.setCellStyle(titleCellStyle);

		cell1 = row.createCell(1);
		cell1.setCellValue("客户名称");
		cell1.setCellStyle(titleCellStyle);

		cell1 = row.createCell(2);
		cell1.setCellValue("联系电话");
		cell1.setCellStyle(titleCellStyle);

		cell1 = row.createCell(3);
		cell1.setCellValue("跟进记录");
		cell1.setCellStyle(titleCellStyle);

		cell1 = row.createCell(4);
		cell1.setCellValue("备注");
		cell1.setCellStyle(titleCellStyle);

		cell1 = row.createCell(5);
		cell1.setCellValue("阶段");
		cell1.setCellStyle(titleCellStyle);

		cell1 = row.createCell(6);
		cell1.setCellValue("跟进人");
		cell1.setCellStyle(titleCellStyle);

		cell1 = row.createCell(7);
		cell1.setCellValue("来源");
		cell1.setCellStyle(titleCellStyle);

		cell1 = row.createCell(8);
		cell1.setCellValue("详情");
		cell1.setCellStyle(titleCellStyle);


		for(int i = 0; i < dataList.size(); i++){
			row = sheet.createRow(i+1);
			Customer cu = dataList.get(i);
			row.createCell(0).setCellValue(i + 1);
			row.createCell(1).setCellValue(cu.getCname());
			row.createCell(2).setCellValue(cu.getTelephone());
			row.createCell(3).setCellValue(cu.getContent());
			row.createCell(4).setCellValue(cu.getRemark());
			if(cu.getStage() !=null && cu.getStage()==0){
				row.createCell(5).setCellValue("沟通");
			} else if (cu.getStage() !=null && cu.getStage()==1) {
				row.createCell(5).setCellValue("意向");
			}else if (cu.getStage() !=null && cu.getStage()==2){
				row.createCell(5).setCellValue("成交");
			}else{
				row.createCell(5).setCellValue("未知");
			}
			row.createCell(6).setCellValue(cu.getBname());
			row.createCell(7).setCellValue(cu.getSource());

			if(cu.getIsSign() !=null && cu.getIsSign()==0){
				row.createCell(8).setCellValue("试发");
			} else if (cu.getIsSign() !=null && cu.getIsSign()==1) {
				row.createCell(8).setCellValue("试发成功");
			}else{
				row.createCell(8).setCellValue("未知");
			}
		}
		String fileName = new String(sheetName);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		byte[] content = os.toByteArray();

		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("applicationnd.ms-excel;charset=utf-8");
		response.setHeader( "Content-Disposition", "attachment;filename=\""+ new String( fileName.getBytes( "gb2312" ), "ISO8859-1" )+ ".xlsx" + "\"");

		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}

	}




	/**
	 * 导出客户管理信息
	 * @param request
	 * @param response
	 * @param dataList
	 */
	public void export1(HttpServletRequest request, HttpServletResponse response,
							Page<Customer> dataList)
	{
		try
		{
			// 构造导出数据
			List<Map<String, Object>> resultList = constructeResultList(dataList);

			String sheetName = "客户管理信息";
			String[] head0 = new String[]
					{ "序号", "客户名称", "联系电话", "跟进记录", "备注", "阶段", "跟进人", "来源","详情"};// 在excel中的第3行每列的参数
			String[] headnum0 = new String[]
					{ "1,2,0,0", "1,2,1,1", "1,2,2,2", "1,2,3,3", "1,2,4,4", "1,2,5,5", "1,2,6,6",
							"1,2,7,7","1,2,8,8"};// 对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
			String[] colName = new String[]
					{
							"number",
							"cname",
							"telephone",
							"content",
							"remark",
							"stage",
							"bname",
							"source",
							"isSignValue"
							};// 需要显示在excel中的参数对应的值，因为是用map存的，放的都是对应的key
			reportXls(response, resultList, sheetName, head0,colName);// utils类需要用到的参数
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("导出失败");
			throw new RuntimeException("导出失败");
		}
	}
	private List<Map<String, Object>> constructeResultList(Page<Customer> dataList)
	{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmpMap = null;
		int number = 0;
		for (Customer customer : dataList )
		{
			number = number++ + 1;
			tmpMap = new HashMap<String, Object>();
			tmpMap.put("number", number);
			tmpMap.put("cname", customer.getCname());
			tmpMap.put("telephone", customer.getTelephone());
			tmpMap.put("content", customer.getContent());
			tmpMap.put("remark", customer.getRemark());
			tmpMap.put("bname", customer.getBname());
			tmpMap.put("source",customer.getSource());

			if(customer.getStage() !=null && customer.getStage()==0){
				tmpMap.put("stage", "沟通");
			} else if (customer.getStage() !=null && customer.getStage()==0) {
				tmpMap.put("stage", "意向");
			}else{
				tmpMap.put("stage", "成交");
			}
			if(customer.getIsSign()!=null){
				if(customer.getIsSign()==0){
					tmpMap.put("isSignValue","试发");
				}else{
					tmpMap.put("isSignValue","成功试发");
				}
			}else{
				tmpMap.put("isSignValue","未知");
			}

			resultList.add(tmpMap);
		}
		return resultList;
	}
	/**
	 * 多行表头 dataList：导出的数据；sheetName：表头名称； head0：表头第一行列名；headnum0：第一行合并单元格的参数
	 *
	 */
	public static void reportXls(HttpServletResponse response,
								 List<Map<String, Object>> dataList, String sheetName, String[] head0,String[] detail) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);// 创建一个表
		// 表头标题样式
		HSSFFont headfont = setHeadFont(workbook);
		HSSFCellStyle headstyle = setHeadStyle(workbook, headfont);
		// 表头时间样式
		HSSFCellStyle style = setStyle(workbook);
		HSSFCellStyle style2 =setStyle2(workbook);
		// 设置列宽 （第几列，宽度）
		setSheetWidthAndHigh(sheet);
		// 第一行表头标题
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, detail.length-1));
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 0x349);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(headstyle);
		cell.setCellValue(sheetName);

		setCell(head0,sheet, style);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 设置列值-内容
		for (int i = 0; i < dataList.size(); i++) {
			row = sheet.createRow(i + 2);
			for (int j = 0; j < detail.length; j++) {
				Map<String, Object> tempmap = dataList.get(i);
				Object data = tempmap.get(detail[j]);
				cell = row.createCell(j);
				cell.setCellStyle(style2);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				if (data instanceof String) {
					cell.setCellValue(data.toString());
				} else if (data instanceof Integer) {
					cell.setCellValue(Integer.valueOf(data.toString()));
				} else if (data instanceof Date) {
					String startTime = sdf.format(data);
					cell.setCellValue(startTime);
				}
			}
		}

		String fileName = new String(sheetName);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		byte[] content = os.toByteArray();

		InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("applicationnd.ms-excel;charset=utf-8");
		response.setHeader( "Content-Disposition", "attachment;filename=\""+ new String( fileName.getBytes( "gb2312" ), "ISO8859-1" )+ ".xlsx" + "\"");

		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}

	}

	private static void setCell(String[] head0,HSSFSheet sheet,
								HSSFCellStyle style)
	{
		HSSFRow row;
		HSSFCell cell;
		row = sheet.createRow(1);
		for (int i = 0; i < 8; i++) {
			cell = row.createCell(i);	// 不需要合并单元格
			cell.setCellValue(head0[i]);	// 列名
			cell.setCellStyle(style);
		}
	}
	private static void setSheetWidthAndHigh(HSSFSheet sheet) {
		sheet.setColumnWidth(0, 1600);
		sheet.setColumnWidth(1, 3600);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4500);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 4500);
		sheet.setColumnWidth(6, 6000);
		sheet.setColumnWidth(7, 2800);
		sheet.setColumnWidth(8, 2800);
		sheet.setDefaultRowHeight((short) 360);// 设置行高
	}

	private static HSSFCellStyle setStyle2(HSSFWorkbook workbook) {
		HSSFFont font2 = setDateFont(workbook);
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style2.setFont(font2);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style2.setWrapText(true); // 换行
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		return style2;
	}

	private static HSSFCellStyle setStyle(HSSFWorkbook workbook) {
		HSSFFont font = setDateFont(workbook);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		style.setLocked(true);
		return style;
	}

	private static HSSFFont setDateFont(HSSFWorkbook workbook) {
		HSSFFont datefont = workbook.createFont();
		datefont.setFontName("宋体");
		datefont.setFontHeightInPoints((short) 12);// 字体大小
		return datefont;
	}

	private static HSSFCellStyle setHeadStyle(HSSFWorkbook workbook, HSSFFont headfont) {
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		return headstyle;
	}

	private static HSSFFont setHeadFont(HSSFWorkbook workbook) {
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 22);// 字体大小
		return headfont;
	}
}
