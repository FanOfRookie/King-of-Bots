package com.kob.backend.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private int id;
    private int sx;
    private int sy;
    private List<Integer> steps;
}
