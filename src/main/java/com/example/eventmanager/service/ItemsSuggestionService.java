package com.example.eventmanager.service;

import com.example.eventmanager.dao.ItemRepository;
import com.example.eventmanager.domain.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemsSuggestionService {

    private final ItemRepository itemRepository;
    private final Logger logger = LogManager.getLogger(ItemService.class);

    @Autowired
    public ItemsSuggestionService(
            ItemRepository itemRepository
    ){
        this.itemRepository = itemRepository;
    }

    public List<Item> getSuggestingItems ( Long userId, Long limit, Long offset ) {
        return null;
    }

    private Map<Long, Integer> getTotalTagsWeight ( Long userId ) {
        Map<Long, Integer> myTagsWeight =
                convertCountToWeight(itemRepository.getWeightOfMyTags(userId), 2);

        Map<Long, Integer> friendsTagsWeight =
                convertCountToWeight(itemRepository.getWeightOfFriendsTags(userId), 1);

        Map<Long, Integer> systemTagsWeight =
                itemRepository.getWeightOfSystemTags(10L, 0L);



        return Stream.of(myTagsWeight, friendsTagsWeight, systemTagsWeight)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                Integer::sum
                        )
                );
    }

    private Map<Long, Integer> convertCountToWeight (Map<Long, Integer> tagsCount, int weightCoeff) {
        int sum = tagsCount.values().stream().reduce(0, Integer::sum);

        return tagsCount.entrySet().stream()
                .collect(
                    Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> ( entry.getValue() * 100 / sum ) * weightCoeff
                    )
                );

    }


}
