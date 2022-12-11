package com.tangeriness.planetarium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;


public class Planetarium implements ModInitializer{
    public static final String MOD_ID = "planetarium";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        LOGGER.info(MOD_ID + " INITEITLIZEDDSD SUCCESS!!!!!!!!!!!!!!!!!!");
    }
}
