package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс запросов вещей
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    /**
     * Получение запросов вещей
     */
    @Override
    public List<ItemRequestDto> getItemRequests(Long from, Long size, Long requestorId) {
        findUser(requestorId);
        List<ItemRequestDto> itemRequestDtos;
        if (from != null && size != null) {
            Pageable pageable = PageRequest.of(
                    from.intValue(), size.intValue(), Sort.by("created").descending());
            Page<ItemRequest> page =
                    itemRequestRepository.findItemRequestsByExcludingRequestorId(requestorId, pageable);
            itemRequestDtos = page.getContent().stream()
                    .map(ItemRequestMapper::toItemRequestDto)
                    .collect(Collectors.toList());
        } else {
            List<ItemRequest> requests =
                    itemRequestRepository.findItemRequestsByExcludingRequestorId(requestorId);
            itemRequestDtos = requests.stream()
                    .map(ItemRequestMapper::toItemRequestDto)
                    .collect(Collectors.toList());
        }
        log.debug("Получение списка запросов: " + itemRequestDtos);
        return itemRequestDtos;
    }

    /**
     * Получение запросов пользователя
     */
    @Override
    public List<ItemRequestDto> getItemRequestsByRequestor(Long requestorId) {
        findUser(requestorId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestorId(requestorId);
        List<ItemRequestDto> itemRequestDtos = requests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        log.debug("Получение списка запросов: " + itemRequestDtos);
        return itemRequestDtos;
    }

    /**
     * Создание запроса вещи
     */
    @Transactional
    @Override
    public ItemRequestDto postItemRequest(ItemRequestDto itemRequestDto, Long requestorId) {
        User user = findUser(requestorId);
        itemRequestDto.setRequestor(user);
        itemRequestDto.setCreated(LocalDateTime.now());
        log.debug("Создание запроса на вещь: " + itemRequestDto);
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto));
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    /**
     * Получение запроса по id
     */
    @Override
    public ItemRequestDto getItemRequestById(Long requestId, Long requestorId) {
        findUser(requestorId);
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException("Запрос с данным id не найден"));
        log.debug("Получение списка запросов: " + request);
        return ItemRequestMapper.toItemRequestDto(request);
    }

    /**
     * Проверка пользователя
     */
    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с данным id не найден"));
    }
}