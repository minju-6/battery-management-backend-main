package wid.bmsbackend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.DeviceMessage;
import wid.bmsbackend.entity.QDeviceMessage;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomDeviceMessageRepositoryImpl implements CustomDeviceMessageRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DeviceMessage> findAllWithSearchOptions(Pageable pageable, SearchOptions searchOptions) {
        QDeviceMessage deviceMessage = QDeviceMessage.deviceMessage;
        BooleanBuilder booleanBuilder = searchOptionCondition(searchOptions, deviceMessage);

        List<DeviceMessage> results = queryFactory.selectFrom(deviceMessage)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(deviceMessage.createdDate.desc())
                .fetch();
        long total = Optional.ofNullable(queryFactory.select(deviceMessage.id.count())
                .from(deviceMessage)
                        .where(booleanBuilder)
                .fetchOne()).orElse(0L);
        return new PageImpl<>(results, pageable, total);
    }

    private BooleanBuilder searchOptionCondition(SearchOptions searchOption, QDeviceMessage deviceMessage) {
        BooleanBuilder builder = new BooleanBuilder();
        if (searchOption.getStatus() != null) {
            builder.and(deviceMessage.status.eq(searchOption.getStatus()));
        }

        if (searchOption.getStartDate() != null && searchOption.getEndDate() != null) {
            builder.and(deviceMessage.createdDate.between(searchOption.getStartDatetime(), searchOption.getEndDatetime()));
        }
        if (searchOption.getEui() != null && !searchOption.getEui().trim().isEmpty()) {
            builder.and(deviceMessage.eui.contains(searchOption.getEui()));
        }
        if(searchOption.getCategoryId() != null && searchOption.getCategoryId() > 0L) {
            builder.and(deviceMessage.eui.in(searchOption.getEuiList()));
        }

        return builder;
    }
}
