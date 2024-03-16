package com.phcworld.freeboard.infrastructure;

import com.phcworld.answer.domain.QFreeBoardAnswer;
import com.phcworld.freeboard.domain.dto.FreeBoardSearch;
import com.phcworld.freeboard.infrastructure.dto.FreeBoardSelect;
import com.phcworld.user.infrastructure.QUserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FreeBoardRepositoryCustomImpl implements FreeBoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QFreeBoardEntity freeBoard = QFreeBoardEntity.freeBoardEntity;
    QUserEntity user = QUserEntity.userEntity;
    QFreeBoardAnswer answer = QFreeBoardAnswer.freeBoardAnswer;

    @Override
    public List<FreeBoardSelect> findByKeyword(FreeBoardSearch searchDto, Pageable pageable){
        List<OrderSpecifier> orders = getOrderSpecifier(pageable);

        List<Long> ids = queryFactory
                .select(freeBoard.id)
                .from(freeBoard)
                .leftJoin(freeBoard.writer, user)
                .where(
                        findByTitle(searchDto),
                        findContents(searchDto),
                        findUserName(searchDto),
                        freeBoard.isDeleted.isFalse())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .fetch();

        return queryFactory
                .select(Projections.fields(FreeBoardSelect.class,
                        freeBoard.id.as("id"),
                        user.as("writer"),
                        freeBoard.title,
                        freeBoard.contents,
                        freeBoard.createDate,
                        freeBoard.updateDate,
                        freeBoard.count,
                        freeBoard.isDeleted,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(answer.count())
                                        .from(answer)
                                        .where(answer.freeBoardEntity.eq(freeBoard)), "countOfAnswer")))
                .from(freeBoard)
                .leftJoin(freeBoard.writer, user)
                .where(freeBoard.id.in(ids))
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .fetch();
    }

    private BooleanExpression findByTitle(FreeBoardSearch searchDto){
        if(Objects.isNull(searchDto.searchType())
                || searchDto.keyword().isEmpty()
                || !searchDto.searchType().equals(0)){
            return null;
        }
        return freeBoard.title.contains(searchDto.keyword());
    }

    private BooleanBuilder findContents(FreeBoardSearch searchDto){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(Objects.isNull(searchDto.searchType())
                || searchDto.keyword().isEmpty()
                || !searchDto.searchType().equals(1)){
            return null;
        }
        return booleanBuilder.or(freeBoard.contents.contains(searchDto.keyword()));
    }

    private BooleanBuilder findUserName(FreeBoardSearch searchDto){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(Objects.isNull(searchDto.searchType())
                || searchDto.keyword().isEmpty()
                || !searchDto.searchType().equals(2)){
            return null;
        }
        return booleanBuilder.or(user.name.contains(searchDto.keyword()));
    }

    private List<OrderSpecifier> getOrderSpecifier(Pageable pageable){
        List<OrderSpecifier> orders = new ArrayList<>();
        if(pageable.getSort() != null){
            for(Sort.Order order : pageable.getSort()){
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()){
                    case "createDate":
                        OrderSpecifier<?> createDate = new OrderSpecifier(direction, freeBoard.createDate);
                        orders.add(createDate);
                        break;
                }
            }
        }
        return orders;
    }
}
