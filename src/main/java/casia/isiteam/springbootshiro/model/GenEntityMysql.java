package casia.isiteam.springbootshiro.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Author wzy
 * Date 2018/2/1 15:09
 * 从数据库表反射出实体类，自动生成实体类
 */
public class GenEntityMysql {

    private String ent_packageOutPath = "main.java.casia.isiteam.springbootshiro.model.po.sysuser.tab";//指定实体生成所在包的路径
    private String jpa_packageOutPath = "main.java.casia.isiteam.springbootshiro.model.po.sysuser.rep";//指定实体jpa生成所在包的路径
    private String authorName = "casia";//作者名字
    private String[] tablenames = {"sys_login_ip"};//要生成实体的表名集合，为空时全部生成

    private boolean f_cover = false;// 是否覆盖已存在的旧实体
    private boolean f_empty = true;// 是否注解不为null
    private boolean f_Serializable = true; //是否实现反编译
    private boolean f_util = false; // 是否需要导入包java.util.*
    private boolean f_sql = false; // 是否需要导入包java.sql.*
    private boolean f_jpa = true; // 是否需要生成基于注解的JPA实体对象
    private boolean f_Repository = true; // 是否需要生成实体操作类

    //记录容器
    private String tablename = "";
    private String[] colnames; // 列名数组
    private String[] colTypes; //列名类型数组
    private int[] colSizes; //列名大小数组
    private boolean[] f_emptys; //注解不为null
    private List<String> key_list = new ArrayList<>();//主键记录
    //数据库连接
//    private static final String URL ="jdbc:mysql://127.0.0.1:3306/sys_user";
//    private static final String NAME = "root";
//    private static final String PASS = "root";
//    private static final String DRIVER ="com.mysql.jdbc.Driver";
    private static final String URL ="jdbc:mysql://106.75.177.129:3308/sys_user";
    private static final String NAME = "web";
    private static final String PASS = "web%2016";
    private static final String DRIVER ="com.mysql.jdbc.Driver";

    /*
     * 构造函数
     */
    public GenEntityMysql(){
        List<String> list=getTableName();
        //创建连接
        Connection con = null;

        for(int p=0;p<list.size();p++){
            tablename=list.get(p);
            //查要生成实体类的表
            String sql = "select * from " + tablename;
            PreparedStatement pStemt = null;

            DatabaseMetaData dbmd = null;
            ResultSet rs = null;

            try {
                try {
                    Class.forName(DRIVER);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                con = DriverManager.getConnection(URL,NAME,PASS);

                dbmd = con.getMetaData();
                rs   = dbmd.getPrimaryKeys(null, null, tablename);//主键
                while (rs.next())  {
                    key_list.add(rs.getString("COLUMN_NAME") );
                }
                rs.close();

                pStemt = con.prepareStatement(sql);
                ResultSetMetaData rsmd = pStemt.getMetaData();
                int size = rsmd.getColumnCount();	//统计列
                colnames = new String[size];
                colTypes = new String[size];
                colSizes = new int[size];
                f_emptys = new boolean[size];
                String databases = "";
                for (int i = 0; i < size; i++) {
                    colnames[i] = rsmd.getColumnName(i + 1);
                    colTypes[i] = rsmd.getColumnTypeName(i + 1);
                    colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
                    f_emptys[i] = rsmd.isNullable(i+1) == 0 ? true : false;
                    if( colTypes[i].equalsIgnoreCase("datetime") || colTypes[i].equalsIgnoreCase("timestamp")  || colTypes[i].equalsIgnoreCase("date") || colTypes[i].equalsIgnoreCase("time") ){
                        f_util = true;
                    }
                    if(colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")  || colTypes[i].equalsIgnoreCase("longtext")){
                        f_sql = true;
                    }
                    databases = rsmd.getCatalogName(i + 1) ;
                }
                pStemt.close();

                String content = parse(colnames,colTypes,colSizes,tablename,databases);

                try {
                    File directory = new File("");
                    String outputPath = directory.getAbsolutePath()+ "/src/"+this.ent_packageOutPath.replace(".", "/")+"/"+cover(0,tablename) + ".java";
                    if( !f_cover ){
                        File  dir = new File( outputPath);
                        if (dir.exists()) {
                            System.out.println("文件："+cover(0,tablename) + ".java 已存在！");
                            continue;
                        }
                    }
                    FileWriter fw = new FileWriter(outputPath);
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(content);
                    pw.flush();
                    pw.close();

                    String outputPath2 = directory.getAbsolutePath()+ "/src/"+this.jpa_packageOutPath.replace(".", "/")+"/"+cover(0,tablename)+"Repository.java";
                    String content2 = parse(tablename,databases);
                    if( !f_cover ){
                        File  dir2 = new File( outputPath2 );
                        if (dir2.exists()) {
                            System.out.println("文件："+cover(0,tablename) + "Repository.java 已存在！");
                            continue;
                        }
                    }
                    if( f_Repository ){
                        FileWriter fw2 = new FileWriter(outputPath2);
                        PrintWriter pw2 = new PrintWriter(fw2);
                        pw2.println(content2);
                        pw2.flush();
                        pw2.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                try {
                    con.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.out.println("生成完毕！");
    }
    /**
     * Java方法 得到当前数据库下所有的表名
     * @param
     */
    private List<String> getTableName() {
        List<String> list=new ArrayList<String>();
        try {
            DatabaseMetaData meta = DriverManager.getConnection(URL,NAME,PASS).getMetaData();
            ResultSet rs = meta.getTables(null, null, null,new String[]{"TABLE"});
            while (rs.next()) {
                if(tablenames==null || tablenames.length == 0){
                    list.add( rs.getString(3) );
                }else if( Arrays.asList(tablenames).contains( rs.getString(3) ) ){
                    list.add( rs.getString(3) );
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 功能：生成实体类主体代码
     * @param colnames
     * @param colTypes
     * @param colSizes
     * @param tablename
     * @param databases
     * @return
     */
    private String parse(String[] colnames, String[] colTypes, int[] colSizes,String tablename ,String databases) {
        StringBuffer sb = new StringBuffer();
        sb.append("package " + this.ent_packageOutPath.replaceAll("main.java.","") + ";\r\n");
        sb.append("\r\n");

        //判断是否导入工具包
        if(f_util){
            sb.append("import java.util.Date;\r\n");
        }
        if(f_sql){
            sb.append("import java.sql.*;\r\n");
        }

        //jpa
        if(f_jpa){
            sb.append("import com.alibaba.fastjson.annotation.JSONField;\r\n");
            sb.append("import javax.persistence.Entity;\r\n");
            sb.append("import javax.persistence.*;\r\n");
            if(f_empty){
                sb.append("import javax.validation.constraints.NotNull;\r\n");
            }
            if(f_Serializable){
                sb.append("import java.io.Serializable;\r\n");
            }
        }

        //注释部分
        sb.append("/**\r\n");
        sb.append(" * "+tablename+" 实体类\r\n");
        sb.append(" * "+new Date()+"\r\n");
        sb.append(" * @"+this.authorName+"\r\n");
        sb.append(" */ \r\n");

        if(f_jpa){
            sb.append("@Entity\r\n");
            sb.append("@Table(name = \""+tablename+"\", schema = \""+databases+"\", catalog = \""+"\")\r\n");
        }
        //实体部分
        sb.append("public class " + cover(0,tablename) + " "+(f_Serializable==true?" implements Serializable ":"")+"{\r\n\r\n");
        processAllAttrs(sb);//属性
        processAllMethod(sb,tablename);//get set方法
//        processAllConstructor(sb,tablename);//toString

        sb.append("}\r\n");

        //System.out.println(sb.toString());
        return sb.toString();
    }
    /**
     * 功能：生成实体操作类主体代码
     * @param tablename
     * @param databases
     * @return
     */
    private String parse(String tablename ,String databases) {
        StringBuffer sb = new StringBuffer();
        sb.append("package " + (this.jpa_packageOutPath).replaceAll("main.java.","") + ";\r\n");
        sb.append("\r\n");

        //jpa
        if(f_jpa){
            sb.append("import org.springframework.data.jpa.repository.JpaRepository;\r\n");
            sb.append("import ").append((this.ent_packageOutPath).replaceAll("main.java.","")).append("."+cover(0,tablename)).append(";\r\n");
        }
        //注释部分
        sb.append("/**\r\n");
        sb.append(" * "+tablename+" 实体操作类\r\n");
        sb.append(" * "+new Date()+"\r\n");
        sb.append(" * @"+this.authorName+"\r\n");
        sb.append(" */ \r\n");
        //实体部分
        sb.append("public interface " + cover(0,tablename) + "Repository extends JpaRepository<"+cover(0,tablename)+",Integer>{\r\n\r\n");
        sb.append("}\r\n");
        return sb.toString();
    }
    /**
     * 功能：生成所有属性
     * @param sb
     */
    private void processAllAttrs(StringBuffer sb) {
        for (int i = 0; i < colnames.length; i++) {
            if( key_list.contains(colnames[i]) ){
                sb.append( "\t@Id\r\n" );
                sb.append( "\t@GeneratedValue(strategy = GenerationType.IDENTITY)\r\n" );
            }
            if( f_empty && f_emptys[i]){
                sb.append( "\t@NotNull\r\n" );
            }
            if( colTypes[i].equalsIgnoreCase("datetime") || colTypes[i].equalsIgnoreCase("timestamp")  ){
                sb.append("\t@JSONField(name=\""+cover(1, colnames[i])+"\",format=\"yyyy-MM-dd HH:mm:ss\")\r\n");
            }else if( colTypes[i].equalsIgnoreCase("date") ){
                sb.append("\t@JSONField(name=\""+cover(1, colnames[i])+"\",format=\"yyyy-MM-dd\")\r\n");
            }else if( colTypes[i].equalsIgnoreCase("time") ){
                sb.append("\t@JSONField(name=\""+cover(1, colnames[i])+"\",format=\"HH:mm:ss\")\r\n");
            }
            sb.append("\t@Column(name = \""+colnames[i]+"\")\r\n");
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + cover(1, colnames[i]) + ";\r\n\r\n");
        }
        sb.append("\r\n");
    }

    /**
     * 功能：生成所有方法
     * @param sb
     */
    private void processAllMethod(StringBuffer sb,String tablename) {
        for (int i = 0; i < colnames.length; i++) {

            if(f_jpa){
                if(i==0){
//                    sb.append("\t@Id\r\n");
//                    sb.append("\t@GeneratedValue(strategy = GenerationType.AUTO)\r\n");
                    sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + cover(0, (colnames[i])) + "(){\r\n");
                }else{
                    sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + cover(0, (colnames[i])) + "(){\r\n");
                }
            }else{
                sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + cover(0, (colnames[i])) + "(){\r\n");
            }
            sb.append("\t\treturn " + cover(1, (colnames[i])) + ";\r\n");
            sb.append("\t}\r\n\r\n");
            sb.append("\tpublic void set" + cover(0, (colnames[i])) + "(" + sqlType2JavaType(colTypes[i]) + " " + cover(1, (colnames[i])) + "){\r\n");
            sb.append("\t\tthis." + cover(1, (colnames[i])) + "=" + cover(1, (colnames[i])) + ";\r\n");
            sb.append("\t}\r\n\r\n");

        }
    }

    public void processAllConstructor(StringBuffer sb,String tablename){
        sb.append("\tpublic ").append(cover(0, tablename)).append("( ");
        for(int i = 0; i < colnames.length; i++){
            sb.append( (i==0?"":", ") + sqlType2JavaType(colTypes[i])).append(" "+cover(1, (colnames[i])) );
        }
        sb.append("){\r\n");
        for (int i = 0; i < colnames.length; i++) {
            sb.append("\t\tthis." + cover(1, (colnames[i])) + "=" + cover(1, (colnames[i])) + ";\r\n");
        }
        sb.append("\t}\r\n");
    }
    /**
     * 功能：获得列的数据类型
     * @param sqlType
     * @return
     */
    private String sqlType2JavaType(String sqlType) {

        if(sqlType.equalsIgnoreCase("bit")){
            return "boolean";
        }else if(sqlType.equalsIgnoreCase("tinyint")){
            return "byte";
        }else if(sqlType.equalsIgnoreCase("smallint")){
            return "short";
        }else if(sqlType.equalsIgnoreCase("int")||sqlType.equalsIgnoreCase("INT UNSIGNED")){
            //INT UNSIGNED无符号整形
            return "int";
        }else if(sqlType.equalsIgnoreCase("bigint")){
            return "long";
        }else if(sqlType.equalsIgnoreCase("float")){
            return "float";
        }else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")){
            return "double";
        }else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text") || sqlType.equalsIgnoreCase("longtext")){
            return "String";
        }else if(sqlType.equalsIgnoreCase("datetime")||sqlType.equalsIgnoreCase("timestamp")|| sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("time")){
            return "Date";
        }else if(sqlType.equalsIgnoreCase("image")){
            return "Blod";
        }
        return null;
    }
    /**
     * 对表名及其字段处理
     * @param type 0-表名，1-字段名
     * @param value 名称
     * @return
     */
    public static String cover(int type ,String value){
        if( type == 0 ){
            String treans = match(value, "^\\S{0,1}|^(_)\\S{0,1}");
            value = value.replaceAll( "^\\S{0,1}|^(_)\\S{0,1}" , initcap(treans).replace("_", "") );
        }
        while (true) {
            String treans = match(value, "(_)\\S{0,1}");
            if(treans == null){ break;}
            value = value.replaceAll( treans , initcap(treans.replace("_", "")) );
        }
        return value;
    }
    /**
     * 正则
     * @param count 内容
     * @param expression 表达式
     * @return
     */
    public static String match(String count, String expression) {
        String result=null;
        if(count==null){
            return result;
        }
        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(count);
        //	boolean r = m.matches();
        if(m.find()) {
            result=m.group();
        }
        return result;
    }
    /**
     * 功能：将输入字符串的首字母改成大写
     * @param str
     * @return
     */
    private static String initcap(String str) {

        char[] ch = str.toCharArray();
        if(ch[0] >= 'a' && ch[0] <= 'z'){
            ch[0] = (char)(ch[0] - 32);
        }

        return new String(ch);
    }
    /**
     * 出口
     * TODO
     * @param args
     */
    public static void main(String[] args) {
        new GenEntityMysql();

    }
}
