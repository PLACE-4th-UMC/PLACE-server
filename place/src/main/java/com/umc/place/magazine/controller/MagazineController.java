package com.umc.place.magazine.controller;

import com.umc.place.common.BaseException;
import com.umc.place.common.BaseResponse;
import com.umc.place.magazine.dto.GetMagazineDetailRes;
import com.umc.place.magazine.service.MagazineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/magazine")
public class MagazineController {

    private final MagazineService magazineService;
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetMagazineDetailRes> getMagazineDetailRes(@PathVariable Long userIdx) throws BaseException {
        return new BaseResponse<>(magazineService.getMagazineDetail(userIdx));
    }

}
