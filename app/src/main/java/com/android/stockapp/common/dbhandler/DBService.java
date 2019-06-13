package com.android.stockapp.common.dbhandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBService {
    private Connection conn=null; //打开数据库对象
    private PreparedStatement ps=null;//操作整合sql语句的对象
    private ResultSet rs=null;//查询结果的集合

    //DBService 对象
    public static DBService dbService=null;

    /**
     * 构造方法 私有化
     * */

    private DBService(){

    }

    /**
     * 获取MySQL数据库单例类对象
     * */
    public static DBService getDbService(){
        if(dbService==null){
            dbService=new DBService();
        }
        return dbService;
    }
    /**
     * 获取stock
     * */

    public List<Klines> getKlinesData(String date){
        //结果存放集合
        List<Klines> list=new ArrayList<Klines>();
        //MySQL 语句
        String sql="select * from date_kines_" + date;
        //获取链接数据库对象
        conn= DBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            Klines u=new Klines();
                            u.setCode(rs.getString("code"));
                            u.setName(rs.getString("name"));
                            u.setDate(rs.getString("date"));
                            u.setOpen(rs.getFloat("open"));
                            u.setClose(rs.getFloat("close"));
                            u.setHigh(rs.getFloat("high"));
                            u.setLow(rs.getFloat("low"));
                            list.add(u);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }

    /**
     * 批量向数据库插入数据   增
     * */

    public int insertKlinesData(List<Klines> list){
        int result=-1;
        if((list!=null)&&(list.size()>0)){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            //MySQL 语句
            String sql="INSERT INTO Klines (name,phone,content,state) VALUES (?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    for(Klines klines:list){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
                        String name=klines.getName();
                        String code=klines.getCode();
                        String date=klines.getDate();
                        float open=klines.getOpen();
                        ps.setString(1,date);
                        ps.setString(2,code);
                        ps.setString(3,name);
                        ps.setString(4,String.valueOf(open));
                        result=ps.executeUpdate();//返回1 执行成功
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }

}
