package com.example.news_aggregator;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.enums.SettingType;
import com.example.news_aggregator.model.settings.Setting;
import com.example.news_aggregator.repository.SettingRepository;
import com.example.news_aggregator.service.setting.SettingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class SettingServiceTest {

    @Mock
    private SettingRepository settingRepository;

    @InjectMocks
    private SettingServiceImpl settingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSettingValueById_Found() {
        // Создаем данные для тестирования
        int id = 1;
        Setting setting = new Setting();
        setting.setId(id);
        setting.setType(SettingType.AUTO_PARSING_FREQUENCY);
        setting.setValue("0 0 8 * * *");

        when(settingRepository.findById(id)).thenReturn(Optional.of(setting));

        // Тест
        String result = settingService.getSettingValueById(id);

        // Проверяем результы тестирования
        assertEquals("0 0 8 * * *", result);
        verify(settingRepository, times(1)).findById(id);
    }

    @Test
    void testGetSettingValueById_NotFound_DefaultValue() {
        // Создаем данные для тестирования
        int id = SettingType.AUTO_PARSING_FREQUENCY.getId();
        when(settingRepository.findById(id)).thenReturn(Optional.empty());

        // Тест
        String result = settingService.getSettingValueById(id);

        // Проверяем результаты тестирования
        assertEquals(SettingType.AUTO_PARSING_FREQUENCY.getDefaultValue(), result);
        verify(settingRepository, times(1)).findById(id);
    }

    @Test
    void testGetSettingValueById_NotFound_Exception() {
        // Создаем данные для тестирования
        int id = 999; // Несуществующий ID
        when(settingRepository.findById(id)).thenReturn(Optional.empty());

        // Тест и проверка результатов тестирования
        NewsAggregatorNotFoundException exception = assertThrows(
                NewsAggregatorNotFoundException.class,
                () -> settingService.getSettingValueById(id)
        );
        assertEquals(String.format(Errors.SETTING_TYPE_ID_S_NOT_FOUND.getDefaultMessage(), id), exception.getMessage());
        verify(settingRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateSetting_ValidCronExpression() {
        // Создаем данные для тестирования
        int id = SettingType.AUTO_PARSING_FREQUENCY.getId();
        String newValue = "0 0 12 * * ?";
        Setting setting = new Setting();
        setting.setId(id);
        setting.setType(SettingType.AUTO_PARSING_FREQUENCY);
        setting.setValue("0 0 * * *");

        when(settingRepository.findById(id)).thenReturn(Optional.of(setting));
        when(settingRepository.save(any(Setting.class))).thenReturn(setting);

        // Тест
        Setting updatedSetting = settingService.updateSetting(id, newValue);

        // Проверяем результаты тестирования
        assertEquals(newValue, updatedSetting.getValue());
        verify(settingRepository, times(1)).findById(id);
        verify(settingRepository, times(1)).save(setting);
    }

    @Test
    void testUpdateSetting_InvalidCronExpression() {
        // Создаем данные для тестирования
        int id = SettingType.AUTO_PARSING_FREQUENCY.getId();
        String invalidValue = "invalid cron";
        Setting setting = new Setting();
        setting.setId(id);
        setting.setType(SettingType.AUTO_PARSING_FREQUENCY);

        when(settingRepository.findById(id)).thenReturn(Optional.of(setting));

        // Тест и проверка результатов тестирования
        NewsAggregatorIllegalArgumentException exception = assertThrows(
                NewsAggregatorIllegalArgumentException.class,
                () -> settingService.updateSetting(id, invalidValue)
        );
        assertEquals(Errors.INVALID_CRON_EXPRESSION.getDefaultMessage(), exception.getMessage());
        verify(settingRepository, times(1)).findById(id);
        verifyNoMoreInteractions(settingRepository);
    }

    @Test
    void testUpdateSetting_SortingType_Valid() {
        // Создаем данные для тестирования
        int id = SettingType.SORTING_TYPE.getId();
        String newValue = "1";
        Setting setting = new Setting();
        setting.setId(id);
        setting.setType(SettingType.SORTING_TYPE);

        when(settingRepository.findById(id)).thenReturn(Optional.of(setting));
        when(settingRepository.save(any(Setting.class))).thenReturn(setting);

        // Тест
        Setting updatedSetting = settingService.updateSetting(id, newValue);

        // Проверяем результаты тестирования
        assertEquals(newValue, updatedSetting.getValue());
        verify(settingRepository, times(1)).save(any(Setting.class));
    }

    @Test
    void testUpdateSetting_NotFound_Exception() {
        // Создаем данные для тестирования
        int id = 999; // Несуществующий ID
        String value = "some value";

        when(settingRepository.findById(id)).thenReturn(Optional.empty());

        // Тест и проверка результатов тестирования
        NewsAggregatorNotFoundException exception = assertThrows(
                NewsAggregatorNotFoundException.class,
                () -> settingService.updateSetting(id, value)
        );
        assertEquals(String.format(Errors.SETTING_TYPE_ID_S_NOT_FOUND.getDefaultMessage(), id), exception.getMessage());
        verify(settingRepository, times(1)).findById(id);
    }
}
