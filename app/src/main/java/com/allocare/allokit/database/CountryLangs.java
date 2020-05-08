package com.allocare.allokit.database;

import com.allocare.allokit.cartmodule.CartModel;

import java.util.List;

public class CountryLangs {
    public CountryLangs(){

    }

        private List<CartModel.Prices> countryLangs;

        public CountryLangs(List<CartModel.Prices> countryLangs) {
            this.countryLangs = countryLangs;
        }

        public List<CartModel.Prices> getCountryLangs() {
            return countryLangs;
        }

        public void setCountryLangs(List<CartModel.Prices> countryLangs) {
            this.countryLangs = countryLangs;
        }

}
