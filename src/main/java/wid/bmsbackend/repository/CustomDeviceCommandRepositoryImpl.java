package wid.bmsbackend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.DeviceCommand;
import wid.bmsbackend.entity.QDeviceCommand;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomDeviceCommandRepositoryImpl implements CustomDeviceCommandRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DeviceCommand> findAllWithSearchOptions(Pageable pageable, SearchOptions searchOptions) {
        QDeviceCommand deviceCommand = QDeviceCommand.deviceCommand;
        BooleanBuilder builder = searchOptionCondition(searchOptions, deviceCommand);

        List<DeviceCommand> results = queryFactory.selectFrom(deviceCommand)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(deviceCommand.createdDate.desc())
                .fetch();

        long total = Optional.ofNullable(queryFactory.select(deviceCommand.id.count())
                .from(deviceCommand)
                .where(builder)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(results, pageable, total);

    }

    private static BooleanBuilder searchOptionCondition(SearchOptions searchOptions, QDeviceCommand deviceCommand) {
        BooleanBuilder builder = new BooleanBuilder();
        if (searchOptions.getStartDate() != null && searchOptions.getEndDate() != null) {
            builder.and(deviceCommand.createdDate.between(searchOptions.getStartDatetime(), searchOptions.getEndDatetime()));
        }
        if (searchOptions.getEui() != null && !searchOptions.getEui().trim().isEmpty()) {
            builder.and(deviceCommand.eui.contains(searchOptions.getEui()));
        }
        if (searchOptions.getCommandCategory() != null) {
            builder.and(deviceCommand.command.stringValue().startsWith(searchOptions.getCommandCategory()));
        }
        if (searchOptions.getCommandAction() != null) {
            builder.and(deviceCommand.command.stringValue().endsWith(searchOptions.getCommandAction()));
        }
        return builder;
    }
}
