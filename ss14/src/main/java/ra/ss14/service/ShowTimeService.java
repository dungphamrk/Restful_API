package ra.ss14.service;

import ra.ss14.model.dto.request.ShowTimeRequestDTO;
import ra.ss14.model.entity.ShowTime;

import java.util.List;

public interface ShowTimeService{
    List<ShowTime> getAllShowTimes();
    ShowTime saveShowTime(ShowTimeRequestDTO showTimeRequestDTO);
}