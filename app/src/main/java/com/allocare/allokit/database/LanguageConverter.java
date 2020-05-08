package com.allocare.allokit.database;

import androidx.room.TypeConverter;

import com.allocare.allokit.cartmodule.CartModel;

import java.util.Arrays;
import java.util.List;

public class LanguageConverter {
        @TypeConverter
        public CountryLangs storedStringToLanguages(CartModel.Prices value) {
            List<CartModel.Prices> langs = Arrays.asList(value);
            return new CountryLangs(langs);
        }

        @TypeConverter
        public String languagesToStoredString(CountryLangs cl) {
            String value = "";

            for (CartModel.Prices lang :cl.getCountryLangs())
                value += lang + ",";

            return value;
        }

}
