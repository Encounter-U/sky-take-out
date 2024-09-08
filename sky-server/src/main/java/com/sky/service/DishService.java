package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @author Encounter
 * @date 2024/09/08 20:34<br/>
 */
public interface DishService
    {
        /**
         * Save with flavor （保存风味）
         *
         * @param dishDTO dish dto
         */
        void saveWithFlavor(DishDTO dishDTO);
    }
