package wid.bmsbackend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import wid.bmsbackend.dto.SearchOptions;
import wid.bmsbackend.entity.LoraMessage;
import wid.bmsbackend.entity.QLoraMessage;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomLoraMessageRepositoryImpl implements CustomLoraMessageRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<LoraMessage> findAllWithSearchOptions(Pageable pageable, SearchOptions searchOptions) {
        QLoraMessage loraMessage = QLoraMessage.loraMessage;
        BooleanBuilder builder = searchOptionCondition(searchOptions, loraMessage);
        List<LoraMessage> results = queryFactory.selectFrom(loraMessage)
                .from(loraMessage)
//                .join(loraMessage.loraRxInfo)
                .where(builder)
                .orderBy(QLoraMessage.loraMessage.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                        queryFactory.select(loraMessage.id.count())
                                .from(loraMessage)
                                .fetchOne())
                .orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

    private static BooleanBuilder searchOptionCondition(SearchOptions messageSearchOptions, QLoraMessage loraMessage) {
        BooleanBuilder builder = new BooleanBuilder();
        if (messageSearchOptions.getStartDate() != null && messageSearchOptions.getEndDate() != null) {
            builder.and(loraMessage.createdDate.between(messageSearchOptions.getStartDatetime(), messageSearchOptions.getEndDatetime()));
        }
        if (messageSearchOptions.getEui() != null) {
            builder.and(loraMessage.devEUI.eq(messageSearchOptions.getEui()));
        }
        return builder;
    }
}
