package com.hdu.rps.service;

import com.hdu.rps.mapper.CountsMapper;
import com.hdu.rps.mapper.PositionMapper;
import com.hdu.rps.mapper.RecommendMapper;
import com.hdu.rps.mapper.RecommendedPersonMapper;
import com.hdu.rps.model.Counts;
import com.hdu.rps.model.Position;
import com.hdu.rps.model.Recommend;
import com.hdu.rps.model.RecommendedPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by SJH on 2017/11/20.
 */
@Service
public class HRDealImpl implements HRDeal {

    private ArrayList<Integer> recommendedPersonIDByPosno;
    private ArrayList<RecommendedPerson> recommendedPersonArrayList = new ArrayList<>();
    private RecommendedPerson recommendedPerson;
    private int index;
    private int recommendedID;
    private Logger logger = Logger.getLogger(String.valueOf(HRDealImpl.this));
    private Recommend recommend;
    private int state;
    private ArrayList<Integer> recommendedPersonIDByPosnoAndState;
    private Position position;
    private int jobCount;
    private Counts counts;

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private RecommendedPersonMapper recommendedPersonMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private CountsMapper countsMapper;


    @Override
    public ArrayList<RecommendedPerson> findRecommendedPersonByPosNo(int posno) {
        recommendedPersonArrayList.clear();
        recommendedPersonIDByPosno = recommendMapper.selectRecommendedPersonIDByPosno(posno);
        if(recommendedPersonIDByPosno.size() <= 0) {
            return null;
        } else {
            for(index = 0;index<recommendedPersonIDByPosno.size();index++) {
                logger.info("----recommendedID:" + index + " : " + recommendedPersonIDByPosno.get(index));
                recommendedID = recommendedPersonIDByPosno.get(index);
                recommendedPerson = recommendedPersonMapper.selectByPrimaryKey(recommendedID);
                recommendedPersonArrayList.add(recommendedPerson);
            }
            return recommendedPersonArrayList;
        }
    }

    @Override
    public void pass(int recommendedPersonID, int positionNo) {
        recommend = recommendMapper.selectByRecommendedNoAndPosNo(recommendedPersonID,positionNo);
        try {
            state = recommend.getRcdstate();
        } catch (NullPointerException nullException) {
            state = 1;
        }
        //推荐人增加积分
        int userNo = recommend.getUserno();
        counts = countsMapper.selectByUserNo(userNo);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        if(counts == null) {
            /*counts = new Counts();
            counts.setUserno(userNo);
            counts.setCountstime(simpleDateFormat.format(date));
            switch (state){
                case 1:
                    counts.setCountsquantity(1);
                    break;
                case 2:
                    counts.setCountsquantity(3);
                    break;
                case 3:
                    counts.setCountsquantity(6);
                    break;
                case 4:
                    counts.setCountsquantity(10);
                    break;
                default:
                    break;
            }
            countsMapper.insert(counts);*/
        } else {
            counts.setCountstime(simpleDateFormat.format(date));
            switch (state){
                case 1:
                    counts.setCountsquantity(1 + countsMapper.selectCountByUserNo(userNo));
                    break;
                case 2:
                    counts.setCountsquantity(2 + countsMapper.selectCountByUserNo(userNo));
                    break;
                case 3:
                    counts.setCountsquantity(3 + countsMapper.selectCountByUserNo(userNo));
                    break;
                case 4:
                    counts.setCountsquantity(4 + countsMapper.selectCountByUserNo(userNo));
                    break;
                default:
                    break;
            }
            countsMapper.updateByPrimaryKeySelective(counts);
        }
        recommend.setRcdstate(state+1);
        if((state + 1) >= 5) {  //通过全部面试
            //职位空缺-1
            position = positionMapper.selectByPrimaryKey(positionNo);
            jobCount = position.getPosstate();
            position.setPosstate(jobCount-1);
            if((jobCount -1) < 0) {
                throw new RuntimeException("岗位余量不足");
            }
            positionMapper.updateByPrimaryKeySelective(position);
            recommendedPerson = recommendedPersonMapper.selectByPrimaryKey(recommendedPersonID);
            recommendedPerson.setRdpincompany(1);
            recommendedPersonMapper.updateByPrimaryKeySelective(recommendedPerson);
        }
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date();
        recommend.setRcdmodtime(simpleDateFormat.format(date));
        recommendMapper.updateByPrimaryKeySelective(recommend);
    }

    @Override
    public void notPass(int recommendedPersonID, int positionNo) {
        recommend = recommendMapper.selectByRecommendedNoAndPosNo(recommendedPersonID,positionNo);
        try {
            state = recommend.getRcdstate();
        } catch (NullPointerException nullException) {
            state = 1;
        }
        recommend.setRcdstate(-1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        recommend.setRcdmodtime(simpleDateFormat.format(date));
        recommendMapper.updateByPrimaryKeySelective(recommend);
    }

    @Override
    public ArrayList<RecommendedPerson> findRecommendedPersonByPosNoAndState(int posno, int state) {
        recommendedPersonArrayList.clear();
        recommendedPersonIDByPosnoAndState = recommendMapper.selectRecommendedPersonIDByPosnoAndState(posno,state);
        if(recommendedPersonIDByPosnoAndState.size() <= 0) {
            return null;    //没人在这个state
        }   else {
            for(index = 0;index < recommendedPersonIDByPosnoAndState.size();index++) {
                recommendedID = recommendedPersonIDByPosnoAndState.get(index);
                recommendedPerson = recommendedPersonMapper.selectByPrimaryKey(recommendedID);
                recommendedPersonArrayList.add(recommendedPerson);
            }
        }
        return recommendedPersonArrayList;
    }

    @Override
    public ArrayList<RecommendedPerson> findPassedPersonByPos(int posno) {
        recommendedPersonArrayList.clear();
        recommendedPersonIDByPosnoAndState = recommendMapper.selectRecommendedPersonIDByPosnoAndState(posno,5);
        for(index = 0;index < recommendedPersonIDByPosnoAndState.size();index++) {
            recommendedID = recommendedPersonIDByPosnoAndState.get(index);
            recommendedPerson = recommendedPersonMapper.selectByPrimaryKey(recommendedID);
            recommendedPersonArrayList.add(recommendedPerson);
        }
        return recommendedPersonArrayList;
    }
}
