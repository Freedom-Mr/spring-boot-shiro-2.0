package casia.isiteam.springbootshiro.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import casia.isiteam.springbootshiro.model.exportPo.AnalysisTaskResultExportPO;




/**
 * 	导出Excel通用方法
 *  @author lnx
 *  @createDate 2018.12.10 
 */
public class ExportExcelUtils<T> {
	private String title;//标题
	
	private String[] rowName;//列名
	
	private List<T> dataList = new ArrayList<>();//数据list
	
	public ExportExcelUtils(String title,String[] rowName,List<T> dataList) {
		 this.dataList = dataList;
	     this.rowName = rowName;
	     this.title = title;
	}
	
	/**
	 * 	导出Excel通用方法
	 */
	public void export(HttpServletResponse response) throws Exception{
		// 创建工作簿对象
		HSSFWorkbook workbook =  new HSSFWorkbook();
		// 创建工作表
		HSSFSheet sheet = workbook.createSheet(title);
		
		// 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);
        
        rowm.setHeight((short) (25 * 35)); //设置高度
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
        HSSFCellStyle style = this.getStyle(workbook); //单元格样式对象

        
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (rowName.length-1)));  
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);
        
        // 定义所需列数
        int columnNum = rowName.length;
        // 在索引2的位置创建行(最顶端的行开始的第二行)
        HSSFRow rowRowName = sheet.createRow(1);                
        rowRowName.setHeight((short) (25 * 25)); //设置高度
        
        // 将列头设置到sheet的单元格中
        for(int n=0;n<columnNum;n++){
            HSSFCell  cellRowName = rowRowName.createCell(n);                //创建列头对应个数的单元格
            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);                //设置列头单元格的数据类型
            HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
            cellRowName.setCellValue(text);                                    //设置列头单元格的值
            cellRowName.setCellStyle(columnTopStyle);                        //设置列头单元格样式
        }
        
      //将查询出的数据设置到sheet对应的单元格中
        for(int i=0;i<dataList.size();i++){
            
            T obj = dataList.get(i);//遍历每个对象
            HSSFRow row = sheet.createRow(i+2);//创建所需的行数
            
            row.setHeight((short) (25 * 20)); //设置高度
            
            Class<? extends Object> cz = obj.getClass();
            Field[] fs = cz.getDeclaredFields();
            for(int j=0; j<=fs.length; j++){
            	
                HSSFCell  cell = null;   //设置单元格的数据类型
                if(j == 0){
                    cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(i+1);    
                }else{
                    cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                    fs[j-1].setAccessible(true);
                	Object o = fs[j-1].get(obj);
                	//设置单元格的值
                	if(o!=null) {
                		cell.setCellValue(o.toString());
                		System.out.println(fs[j-1].getName()+":"+o.toString());
                	}else {
                		cell.setCellValue("");
                	}
                }
                cell.setCellStyle(style);                                    //设置单元格样式
            }
        }
        
        //让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if(colNum == 0){
                sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
            }else{
            	int width = (columnWidth+4) * 256;
                sheet.setColumnWidth(colNum, width>255 * 256?6000:width);
               // sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
            }
        }
        
        if(workbook !=null){
        	OutputStream out = null;
            try {
                String fileName = "Excel-" + new Date().getTime() + ".xls";
                String headStr = "attachment; filename=\"" + fileName + "\"";
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                response.setCharacterEncoding("UTF-8");
                out = response.getOutputStream();
                workbook.write(out);
            }catch (IOException e){
                e.printStackTrace();
            }finally {
            	if(out!=null) {
            		out.close();
            	}
            }
        }
	}
	
	
	/**
	 * 	导出分析结果result Excel方法<p>
	 *	处理实体中的json数据，该数据字段必须在最后一个
	 * 	@param resultId 结果id
	 * 	
	 */
	public void export(HttpServletResponse response ,Integer resultId) throws Exception{
		// 创建工作簿对象
		HSSFWorkbook workbook =  new HSSFWorkbook();
		workbook = setSheet(workbook, resultId);
        if(workbook !=null){
        	OutputStream out = null;
            try {
                String fileName = "Excel-" + new Date().getTime() + ".xls";
                String headStr = "attachment; filename=\"" + fileName + "\"";
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                out = response.getOutputStream();
                workbook.write(out);
            }catch (IOException e){
                e.printStackTrace();
            }finally {
            	if(out!=null) {
            		out.close();
            	}
            }
        }
	}
	
	/**
	 * 	导出全部分析结果result Excel方法<p>
	 *	处理实体中的json数据，该数据字段必须在最后一个
	 * 	@param map key为结果id,value为导出分析结果实体list
	 * 	
	 */
	@SuppressWarnings("unchecked")
	public void exportAll(HttpServletResponse response ,Map<Integer,List<AnalysisTaskResultExportPO>> map)throws Exception{
		// 创建工作簿对象
		HSSFWorkbook workbook =  new HSSFWorkbook();
		for(Map.Entry<Integer,List<AnalysisTaskResultExportPO>> entry : map.entrySet()) {
			this.dataList = (List<T>) entry.getValue();
			this.rowName = getRowNameByResultId(entry.getKey());
			this.title = TypeUtils.getAnalysisResultType(entry.getKey());
			setSheet(workbook,entry.getKey());
		}
		 if(workbook !=null){
	        	OutputStream out = null;
	            try {
	                String fileName = "Excel-" + new Date().getTime() + ".xls";
	                String headStr = "attachment; filename=\"" + fileName + "\"";
	                response.setContentType("APPLICATION/OCTET-STREAM");
	                response.setHeader("Content-Disposition", headStr);
	                out = response.getOutputStream();
	                workbook.write(out);
	            }catch (IOException e){
	                e.printStackTrace();
	            }finally {
	            	if(out!=null) {
	            		out.close();
	            	}
	            }
	        }
	}
	 
	/**
	 * 	设置结果分析Sheet信息
	 * 	@param workbook
	 * 	@param resultId 
	 */
	public HSSFWorkbook setSheet(HSSFWorkbook workbook,Integer resultId) throws Exception{
		//提示说明
		String tip = TypeUtils.getTipByResultId(resultId);
		// 创建工作表
		HSSFSheet sheet = workbook.createSheet(title);
		
		// 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);
        
        rowm.setHeight((short) (30 * 35)); //设置高度
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获样式取列头对象
        HSSFCellStyle style = this.getStyle(workbook); //单元格样式对象

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (rowName.length-1)));  
        cellTiltle.setCellStyle(columnTopStyle);
        
        String top = title+"\r\n"+tip;
        HSSFRichTextString richString = new HSSFRichTextString(top);
        Font font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        font.setFontHeightInPoints((short)11);
	    //字体加粗
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    //设置字体名字 
	    font.setFontName("Courier New");
        richString.applyFont(title.length()+1, top.length(), font);
        cellTiltle.setCellValue(richString);
        
        // 定义所需列数
        int columnNum = rowName.length;
        // 在索引2的位置创建行(最顶端的行开始的第二行)
        HSSFRow rowRowName = sheet.createRow(1);                
        rowRowName.setHeight((short) (25 * 25)); //设置高度
        
        // 将列头设置到sheet的单元格中
        for(int n=0;n<columnNum;n++){
            HSSFCell  cellRowName = rowRowName.createCell(n);                //创建列头对应个数的单元格
            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);                //设置列头单元格的数据类型
            HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
            cellRowName.setCellValue(text);                                    //设置列头单元格的值
            cellRowName.setCellStyle(columnTopStyle);                        //设置列头单元格样式
        }
        
      //将查询出的数据设置到sheet对应的单元格中
        for(int i=0;i<dataList.size();i++){
            
            T obj = dataList.get(i);//遍历每个对象
            HSSFRow row = sheet.createRow(i+2);//创建所需的行数
            
            row.setHeight((short) (25 * 25)); //设置高度
            
            Class<? extends Object> cz = obj.getClass();
            Field[] fs = cz.getDeclaredFields();
            for(int j=0; j<=fs.length; j++){
            	
                HSSFCell  cell = null;   //设置单元格的数据类型
                if(j == 0){
                    cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(i+1);    
                }else if(j==fs.length){
                	if(resultId!=null && resultId >0) {
                		fs[j-1].setAccessible(true);
                		Object o = fs[j-1].get(obj);
                		JSONObject json = toJSON(o.toString());
                		/*if(resultId == 16){
                			String[] fields = {"regTime","mobilePhone","nickname","email","regIp"};
                			for(String field:fields) {
                				cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                				if(json.containsKey(field)) {
                					cell.setCellValue(json.get(field).toString());
                				}else {
                					cell.setCellValue("");
                				}
                				j++;
                				cell.setCellStyle(style);
                			}
                        }else if(resultId == 23) {
                        	String[] fields = {"createTime","createUser"};
                			for(String field:fields) {
                				cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                				if(json.containsKey(field)) {
                					cell.setCellValue(json.get(field).toString());
                				}else {
                					cell.setCellValue("");
                				}
                				j++;
                				cell.setCellStyle(style);
                			}
                        }else {
                        	for(String str : json.keySet()) {
	                			cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
	                			String value = json.get(str).toString();
	                			if(str == "friends" || str == "groups" || str == "users") {
	                				JSONArray ja = JSONArray.parseArray(value);
	                				value = "";
	                				for(Object ob : ja) {
	                					value+=","+ob.toString();
	                				}
	                				value = value.substring(1);
                    			}
	                			cell.setCellValue(value);
	                			j++;
	                			cell.setCellStyle(style);
	                		}
                        }*/
                		switch (resultId) {
                			case 10:case 11:case 12:
                				String[] fields = {"firndsNum","friends"};
                				for(String field:fields) {
                    				cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                    				
                    				if(json.containsKey(field)) {
                    					String value = json.get(field).toString();
                    					if(field == "friends"){
                    						JSONArray ja = JSONArray.parseArray(value);
	    	                				value = "";
	    	                				for(Object ob : ja) {
	    	                					value+=","+ob.toString();
	    	                				}
	    	                				value = value.substring(1);
                    					}
                    					cell.setCellValue(value);
                    				}else {
                    					cell.setCellValue("");
                    				}
                    				j++;
                    				cell.setCellStyle(style);
                    			}
                				break;
                			case 13:case 14:case 21:case 34:
                				String[] groupsFields = {"groupsNum","groups"};
                				for(String field:groupsFields) {
                    				cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                    				
                    				if(json.containsKey(field)) {
                    					String value = json.get(field).toString();
                    					if(field == "groups"){
                    						JSONArray ja = JSONArray.parseArray(value);
	    	                				value = "";
	    	                				for(Object ob : ja) {
	    	                					value+=","+ob.toString();
	    	                				}
	    	                				value = value.substring(1);
                    					}
                    					cell.setCellValue(value);
                    				}else {
                    					cell.setCellValue("");
                    				}
                    				j++;
                    				cell.setCellStyle(style);
                    			}
                				break;
                			case 15:case 20:case 22:case 32:case 33:
                				String[] usersFields = {"usersNum","users"};
                				for(String field:usersFields) {
                    				cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                    				
                    				if(json.containsKey(field)) {
                    					String value = json.get(field).toString();
                    					if(field == "users"){
                    						JSONArray ja = JSONArray.parseArray(value);
	    	                				value = "";
	    	                				for(Object ob : ja) {
	    	                					value+=","+ob.toString();
	    	                				}
	    	                				value = value.substring(1);
                    					}
                    					cell.setCellValue(value);
                    				}else {
                    					cell.setCellValue("");
                    				}
                    				j++;
                    				cell.setCellStyle(style);
                    			}
                				break;
                			case 16:
                				String[] friendsFields = {"regTime","mobilePhone","nickname","email","regIp"};
                    			for(String field:friendsFields) {
                    				cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                    				if(json.containsKey(field)) {
                    					cell.setCellValue(json.get(field).toString());
                    				}else {
                    					cell.setCellValue("");
                    				}
                    				j++;
                    				cell.setCellStyle(style);
                    			}
                    			break;
                			case 23:
	                			String[] createrFields = {"createTime","createUser"};
	                			for(String field:createrFields) {
	                				cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
	                				if(json.containsKey(field)) {
	                					cell.setCellValue(json.get(field).toString());
	                				}else {
	                					cell.setCellValue("");
	                				}
	                				j++;
	                				cell.setCellStyle(style);
	                			}
	                			break;
	                		default	:
	                			for(String str : json.keySet()) {
		                			cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
		                			cell.setCellValue(json.get(str).toString());
		                			j++;
		                			cell.setCellStyle(style);
		                		}
	                			break;
                		}
                    }
                	
                }else{
                    cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
                    fs[j-1].setAccessible(true);
                	Object o = fs[j-1].get(obj);
                	//设置单元格的值
                	if(o!=null) {
                		cell.setCellValue(o.toString());
                	//	System.out.println(fs[j-1].getName()+":"+o.toString());
                	}else {
                		cell.setCellValue("");
                	}
                }
                cell.setCellStyle(style);                                    //设置单元格样式
            }
        }
        
        //让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if(colNum == 0){
                sheet.setColumnWidth(colNum, 20 * 256);
            }else{
            	int width = (columnWidth+4) * 256;
                sheet.setColumnWidth(colNum, width>255 * 256?6000:width);
            }
        }
        return workbook;
	}
	
	/*
	 * 列头单元格样式
	 * */
	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
	      // 设置字体
	      HSSFFont font = workbook.createFont();
	      //设置字体大小
	      font.setFontHeightInPoints((short)11);
	      //字体加粗
	      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	      //设置字体名字 
	      font.setFontName("Courier New");
	      //设置样式; 
	      HSSFCellStyle style = workbook.createCellStyle();
	      //设置底边框; 
	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      //设置底边框颜色;  
	      style.setBottomBorderColor(HSSFColor.BLACK.index);
	      //设置左边框;   
	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      //设置左边框颜色; 
	      style.setLeftBorderColor(HSSFColor.BLACK.index);
	      //设置右边框; 
	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      //设置右边框颜色; 
	      style.setRightBorderColor(HSSFColor.BLACK.index);
	      //设置顶边框; 
	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      //设置顶边框颜色;  
	      style.setTopBorderColor(HSSFColor.BLACK.index);
	      //在样式用应用设置的字体;  
	      style.setFont(font);
	      //设置自动换行; 
	      style.setWrapText(true);
	      //设置水平对齐的样式为居中对齐;  
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      //设置垂直对齐的样式为居中对齐; 
	      style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	      
	      //设置单元格背景颜色
	      style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      return style;
	      
	  }
      
	  
	/*  
	 * 列数据信息单元格样式
	 */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
          // 设置字体
          HSSFFont font = workbook.createFont();
          //设置字体大小
          //font.setFontHeightInPoints((short)10);
          //字体加粗
          //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
          //设置字体名字 
          font.setFontName("Courier New");
          //设置样式; 
          HSSFCellStyle style = workbook.createCellStyle();
          //设置底边框; 
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
          //设置底边框颜色;  
          style.setBottomBorderColor(HSSFColor.BLACK.index);
          //设置左边框;   
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
          //设置左边框颜色; 
          style.setLeftBorderColor(HSSFColor.BLACK.index);
          //设置右边框; 
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);
          //设置右边框颜色; 
          style.setRightBorderColor(HSSFColor.BLACK.index);
          //设置顶边框; 
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);
          //设置顶边框颜色;  
          style.setTopBorderColor(HSSFColor.BLACK.index);
          //在样式用应用设置的字体;  
          style.setFont(font);
          //设置自动换行; 
          style.setWrapText(false);
          //设置水平对齐的样式为居中对齐;  
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
          //设置垂直对齐的样式为居中对齐; 
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         
          return style;
    }
    
    /**
	 * 	将json字符串数据转为json
	 * 	@param data json字符串数据
	 * 	
	 */
    public static JSONObject toJSON(String data) {
    	return JSONObject.parseObject(data);
    }
    
    
    /**
	 * 	根据resultId获取 列名
	 * 	@param resultId 结果ID
	 * 	
	 */
    public static String[] getRowNameByResultId(Integer resultId) {
    	String[] rowName = {"序号","任务名称","分析类型","数据类型","分析结果类型","账号","微信账号"};
    	List<String> list = new ArrayList<>(Arrays.asList(rowName));
    	switch(resultId) {
	    	case 10:  
	    		list.add("好友数量");
	    		list.add("具体账号");
	    		break;
	    	case 11:
	    		list.add("共同好友数量");
	    		list.add("具体账号");
	    		break;
	    	case 12:
	    		list.add("非好友量");
	    		list.add("具体账号");
	    		break;
			case 13: 
				list.add("加入群数量");
				list.add("具体群号");
				break;
			case 14:
				list.add("共同加入群数量");
				list.add("具体群号");
				break;
			case 15: case 20: case 32:
				list.add("群成员数量");
				list.add("具体账号");
				break;
			case 16:
				list.add("账号创建时间");
				list.add("联系电话");
				list.add("名称");
				list.add("邮箱");
				list.add("创建ip地址");
				break;
			case 21: case 34:
				list.add("群成员加入的群数");
				list.add("具体群号");
				break;
			case 22: case 33:
				list.add("群共同成员数量");
				list.add("具体账号");
				break;
			case 23:
				list.add("群创建时间");
				list.add("群创建人");
				break;
			case 30:
				list.add("群信息数量");
				break;
			case 31:
				list.add("群线索信息数量");
				break;
			case 35:
				list.add("成员发布的线索信息数量");
				break;
			case 36:
				list.add("群聊共性发现归类");
				break;
			default:
		}
		return list.toArray(new String[list.size()]);
    }
}
