package com.jumpjump.bullhorsebot.bean;

import lombok.Data;

import java.util.List;

@Data
public class User {

        private long userId;
        private String avatar;
        private String userName;
        private long id;
        private int rank;
        private String rankImg;
        private int skill;
        private double scorePerMinute;
        private double killsPerMinute;
        private String winPercent;
        private String bestClass;
        private String accuracy;
        private String headshots;
        private String timePlayed;
        private long secondsPlayed;
        private double killDeath;
        private double infantryKillDeath;
        private double infantryKillsPerMinute;
        private long kills;
        private int deaths;
        private int wins;
        private int loses;
        private int longestHeadShot;
        private int revives;
        private int dogtagsTaken;
        private int highestKillStreak;
        private int roundsPlayed;
        private long awardScore;
        private long bonusScore;
        private long squadScore;
        private long currentRankProgress;
        private long totalRankProgress;
        private int avengerKills;
        private int saviorKills;

        private int headShots;
        private long heals;
        private int repairs;
        private int killAssists;
        private List<Vehicles> vehicles; // 载具对象

        private List<Weapon> weapons; // 武器对象

}

