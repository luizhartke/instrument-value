package com.test.instrumentValue.endpoint;

import java.util.List;

import com.test.instrumentValue.jpa.InstrumentPriceStatistics;
import com.test.instrumentValue.service.InstrumentCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstrumentStatisticsEndpoint {

    @Autowired
    private InstrumentCalculatorService instrumentCalculatorService;

    @RequestMapping(value = "/instrument-value/run", method = RequestMethod.GET)
    public void launchStatistics() {
        instrumentCalculatorService.runStatistics();
    }

    @RequestMapping(value = "/instrument-value/instrument3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstrumentPriceStatistics> instrument3OnTheFly() {
        return new ResponseEntity<>(instrumentCalculatorService.getInstrument3StatisticsOnTheFly(), HttpStatus.OK);
    }

    @RequestMapping(value = "/instrument-value/fullStatistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InstrumentPriceStatistics>> fullStatistics() {
        return new ResponseEntity<>(instrumentCalculatorService.getInstrumentStatistics(), HttpStatus.OK);
    }
}
