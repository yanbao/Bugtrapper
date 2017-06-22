package com.bugtrapper01.transfer.rest;

import com.bugtrapper01.transfer.util.MathUtil;
import com.bugtrapper01.transfer.util.MongoUtil;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 大泥藕 on 2017/5/17.
 */

@RestController
public class CrDaily {

    @Autowired
    private MongoUtil mongoUtil;


    @PostMapping(value = "/corrDaily")
    public List crDaily(@RequestParam(value = "token", required = true) String token,
                                 @RequestParam(value = "tid", required = true) String tid,
                                 @RequestParam(value = "min", required = true) int min,//更正数据最小值
                                 @RequestParam(value = "max", required = true) int max,//更正数据最大值
                                 @RequestParam(value = "auto", required = true) int auto,//是否使用随机数，非0开启
                                 @RequestParam(value = "n", required = true) int n,//随机数最小值
                                 @RequestParam(value = "m", required = true) int m//随机数最大值

    ) {
        if (!(token.equals("zmkm"))) {
            List fl = new ArrayList<>();
            Map fmap = new HashMap();
            fmap.put("code", 50);
            fmap.put("isSuccess", "请输入正确口令");
            fl.add(fmap);
            return fl;
        }

        //----------------------------------------------------------
        List nl = new ArrayList<>();
        BasicDBObject squery = new BasicDBObject("t_id", Long.parseLong(tid));
        List<BasicDBObject> sObjs = mongoUtil.find("daily_data", squery);
        SimpleDateFormat fdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat ffdf = new SimpleDateFormat("yyyy-MM-dd");
        try{



            if(!MongoUtil.isEmpty(sObjs)){
                Date t_date = null;
                for(BasicDBObject sObj :sObjs) {
                    Map smap = new HashMap();
                    ObjectId _id = sObj.getObjectId("_id");

                    t_date = sObj.getDate("t_date");
                    if (t_date==null){
                        continue;
                    }
                    String fd = fdf.format(t_date);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fdf.parse(fd));
                    cal.add(Calendar.HOUR_OF_DAY, 0);
                    String tDate = fdf.format(cal.getTime());
                    Date point = ffdf.parse(tDate);

                    smap.put("_id",_id.toHexString());
                    smap.put("before", fd);
                    smap.put("after",ffdf.format(point));





                    int fValue = sObj.getInt("t_value");
                    smap.put("t_value",fValue);
                    if (fValue<min||fValue>max){
                        Document query = new Document();
                        query.put("_id", _id);

                        int fn = 0;
                        if (auto!=0){
                            fn = MathUtil.getRandomInt(n,m);
                        }
                        BasicDBObject fresult = new  BasicDBObject("t_value", fn);;
                        boolean ret = mongoUtil.update("daily_data", query, fresult,false);
                        if(ret){
                            smap.put("t_valueAfter",fn);
                        }
                    }
                    nl.add(smap);
                }

            }



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nl;
    }
}