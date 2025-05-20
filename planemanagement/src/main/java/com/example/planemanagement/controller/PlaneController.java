package com.example.planemanagement.controller;

import com.example.planemanagement.model.Person;
import com.example.planemanagement.model.Ticket;
import com.example.planemanagement.service.PlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = {"http://localhost:5174", "https://plane-ticket-management.vercel.app"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS},
        allowedHeaders = "*")
public class PlaneController {

    @Autowired
    private PlaneService planeService;

    @GetMapping
    public int[][] getSeatingPlan() {
        return planeService.getSeatingPlan();
    }

    @PostMapping("/book")
    public Map<String, Object> buySeat(@RequestBody SeatRequest request) {
        return planeService.buySeat(request.getRow(), request.getSeat(), request.getPerson());
    }

    @PostMapping("/cancel")
    public Map<String, Object> cancelSeat(@RequestBody SeatRequest request) {
        return planeService.cancelSeat(request.getRow(), request.getSeat());
    }

    @GetMapping("/first-available")
    public Map<String, Object> findFirstAvailable() {
        return planeService.findFirstAvailable();
    }

    @GetMapping("/tickets")
    public List<Ticket> getTickets() {
        return planeService.getTickets();
    }

    @GetMapping("/search")
    public Map<String, Object> searchTicket(@RequestParam String row, @RequestParam int seat) {
        return planeService.searchTicket(row, seat);
    }
}

class SeatRequest {
    private String row;
    private int seat;
    private Person person;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
