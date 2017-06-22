package com.bugtrapper01.transfer.rest;

import com.bugtrapper01.transfer.util.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大泥藕 on 2017/6/19.
 */

@RestController
public class BatchSyn {


    @Autowired
    SyDaily syDaily;
    @Autowired
    CrDaily crDaily;
    @Autowired
    CrDeviated crDeviated;

    @PostMapping (value = "/batSyn")
    public List batSyn(@RequestParam(value = "token", required = true) String token){

        List fl = new ArrayList();
        String[] fa = {"2061","2063","1413","1415","1959","3099","2996","3032","2995","2943","0"};

        for (int i=0;i<fa.length;i++) {

            String tid = fa[i];
            crDeviated.crDeviate(token,tid,-5,150,1,0,10);
            crDaily.crDaily(token,tid,-5,150,1,0,10);
            syDaily.syDaily(token,tid);
            fl.add(tid);
        }


        return fl;
    }
    @PostMapping (value = "/batSyn2")
    public void batSyn2(@RequestParam(value = "token", required = true) String token){


//        com.nbicc.utils.TimerUtil.Timer();
        List<String> l = com.nbicc.trasfer.BatchSyn.batSyn("zmkm");
        if (!MongoUtil.isEmpty(l)){
            for (String s:l){
                System.out.println(s);
            }

        }else {
            System.out.println("false");
        }
    }
}
