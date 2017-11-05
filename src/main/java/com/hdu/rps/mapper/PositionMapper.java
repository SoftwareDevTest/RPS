package com.hdu.rps.mapper;

import com.hdu.rps.model.Position;

public interface PositionMapper {
    int deleteByPrimaryKey(Integer posno);

    int insert(Position record);

    int insertSelective(Position record);

    Position selectByPrimaryKey(Integer posno);

    int updateByPrimaryKeySelective(Position record);

    int updateByPrimaryKey(Position record);
}