package com.example.eventmanager.service;

import com.example.eventmanager.dao.ItemRepository;
import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.ItemsTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemsSuggestionService {

    private final ItemRepository itemRepository;
    private final Logger logger = LogManager.getLogger(ItemService.class);

    private final int LIMIT_SYSTEM_TAGS = 100;

    @Autowired
    public ItemsSuggestionService(
            ItemRepository itemRepository
    ){
        this.itemRepository = itemRepository;
    }

    public List<Item> getSuggestingItems ( Long userId, Long limit ) {

        Map<Long, Integer> weight = getTotalTagsWeight( userId );

        List<ItemsTag> items = itemRepository.getItemsWithTags( weight.keySet(), userId );

        return getSortedItemsByWeight(weight, items, limit);
    }

    private List<Item> getSortedItemsByWeight ( Map<Long, Integer> tagsWeight, List<ItemsTag> tagsWithItems, Long limit) {

        Map<Item, Integer> itemWeight = tagsWithItems.stream()
                .collect(
                        Collectors.toMap(
                                ItemsTag::getItem,
                                tagsItem -> tagsWeight.get(tagsItem.getTagId()),
                                Integer::sum
                        )
                );

        return itemWeight.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());

    }


    private Map<Long, Integer> getTotalTagsWeight ( Long userId ) {
        Map<Long, Integer> myTagsWeight =
                convertCountToWeight(itemRepository.getWeightOfMyTags(userId), 2);

        Map<Long, Integer> friendsTagsWeight =
                convertCountToWeight(itemRepository.getWeightOfFriendsTags(userId), 1);

        Map<Long, Integer> systemTagsWeight =
                itemRepository.getWeightOfSystemTags(LIMIT_SYSTEM_TAGS );



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
