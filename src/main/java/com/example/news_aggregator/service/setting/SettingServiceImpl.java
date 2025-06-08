package com.example.news_aggregator.service.setting;

import com.example.news_aggregator.common.exception.NewsAggregatorIllegalArgumentException;
import com.example.news_aggregator.common.exception.NewsAggregatorNotFoundException;
import com.example.news_aggregator.enums.Errors;
import com.example.news_aggregator.enums.SettingType;
import com.example.news_aggregator.model.settings.Setting;
import com.example.news_aggregator.output.screen.NewsDynamicMenuFactory;
import com.example.news_aggregator.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class SettingServiceImpl implements SettingService {

    // Регулярное выражение для проверки шестизначного cron выражения
    private static final String CRON_REGEX = "^([0-5]?\\d|\\*)\\s+([0-5]?\\d|\\*)\\s+([01]?\\d|2[0-3]|\\*)\\s+([1-9]|[12]\\d|3[01]|\\*|\\?|L|W)\\s+(1[0-2]|0?[1-9]|\\*|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\s+([0-7]|MON|TUE|WED|THU|FRI|SAT|SUN|\\*|\\?|L)$";
    private static final Pattern CRON_PATTERN = Pattern.compile(CRON_REGEX);

    private final SettingRepository settingRepository;

    @Autowired
    public SettingServiceImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public String getSettingValueById(int id) {
        Optional<Setting> setting = settingRepository.findById(id);
        if (setting.isEmpty()) {
            if (id == SettingType.AUTO_PARSING_FREQUENCY.getId()) {
                return SettingType.AUTO_PARSING_FREQUENCY.getDefaultValue();
            }
            throw new NewsAggregatorNotFoundException(Errors.SETTING_TYPE_ID_S_NOT_FOUND, id);
        }
        return setting.get().getValue();
    }

    @Override
    public Setting updateSetting(int id, String value) {
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new NewsAggregatorNotFoundException(Errors.SETTING_TYPE_ID_S_NOT_FOUND, id));
        if (setting.getType() == SettingType.AUTO_PARSING_FREQUENCY) {
            boolean isValidValue = isValidCronExpression(value);
            if (isValidValue) {
                setting.setValue(value);
                return settingRepository.save(setting);
            } else {
                throw new NewsAggregatorIllegalArgumentException(Errors.INVALID_CRON_EXPRESSION);
            }
        } else if (setting.getType() == SettingType.SORTING_TYPE) {
            NewsDynamicMenuFactory.SortOrder sortOrder = validateSortingType(value);
            Optional<Setting> sortingSettingOptional = settingRepository.findById(SettingType.SORTING_TYPE.getId());

            if (sortingSettingOptional.isPresent()) {
                Setting sortingSetting = sortingSettingOptional.get();
                sortingSetting.setValue(String.valueOf(sortOrder.getId()));
                return settingRepository.save(sortingSetting);
            } else {
                String settingValue = SettingType.SORTING_TYPE.getDefaultValue();
                Setting newSetting = new Setting(2, SettingType.SORTING_TYPE, settingValue);
                return settingRepository.save(newSetting);
            }
        }
        return null;
    }

    /**
     * Метод для проверки корректности шестизначного cron выражения.
     *
     * @param cronExpression строка cron выражения
     * @return true, если cron выражение корректно, иначе false
     */
    private boolean isValidCronExpression(String cronExpression) {
        if (cronExpression == null || cronExpression.isEmpty()) {
            return false;
        }
        return CRON_PATTERN.matcher(cronExpression).matches();
    }

    /**
     * Метод для проверки корректности ввода метода сортировки. Возвращает SortOrder в случае верного ввода,
     *
     * @param sortingType строка cron выражения
     * @return SortOrder в случае верного ввода, иначе - NewsAggregatorIllegalArgumentException
     * @throws NewsAggregatorIllegalArgumentException - в случае неверного ввода
     */
    private NewsDynamicMenuFactory.SortOrder validateSortingType(String sortingType) {
        try {
            return NewsDynamicMenuFactory.SortOrder.getById(Integer.parseInt(sortingType));
        } catch (NumberFormatException e) {
            throw new NewsAggregatorIllegalArgumentException(Errors.INVALID_NUMBER_FORMAT);
        }
    }
}
