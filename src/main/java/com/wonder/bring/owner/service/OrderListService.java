package com.wonder.bring.owner.service;

import com.wonder.bring.owner.dto.OrderList;
import com.wonder.bring.owner.mapper.OrderListMapper;
import com.wonder.bring.owner.model.DefaultRes;
import com.wonder.bring.owner.utils.Message;
import com.wonder.bring.owner.utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by bomi on 2019-01-04.
 */

@Slf4j
@Service
public class OrderListService {
    private final OrderListMapper orderListMapper;

    public OrderListService(final OrderListMapper orderMapper) {
        this.orderListMapper = orderMapper;
    }

    /**
     * 주문내역들 조회
     */
    private static List<OrderList> list = new LinkedList<>();

    public DefaultRes<List<OrderList>> getOrderList(final int storeIdx) {
        list = orderListMapper.getAllOrderLists(storeIdx);

        if(orderListMapper.checkIdx(storeIdx) == 0) {
            return DefaultRes.res(Status.NOT_FOUND, Message.NOT_STORE_FOUND);
        }

        if(list.isEmpty())
            return DefaultRes.res(Status.NO_CONTENT, "주문내역이 없습니다");

        return DefaultRes.res(Status.SUCCESS, "리스트 조회 성공", list);
    }

    /**
     * 상태 정보 변경
     * @param orderIdx
     * @param state
     * @return
     */
    public DefaultRes updateOrderState(final int orderIdx, final Optional<Integer> state) {
       try {
           if(state.isPresent()) {
               if(orderListMapper.checkIdx(orderIdx) == 0) {
                   return DefaultRes.res(Status.NOT_FOUND, Message.NOT_ORDER_FOUND);
               }
               orderListMapper.changeState(orderIdx, state.get());
               return DefaultRes.res(Status.SUCCESS, "상태 변경 성공");
           }
           else
               return DefaultRes.res(Status.BAD_REQUEST, Message.BAD_REQUEST);
       }catch(Exception e) {
           return DefaultRes.res(Status.DB_ERROR, Message.DB_ERROR);
       }
    }

}
