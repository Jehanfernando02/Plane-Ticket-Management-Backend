package com.example.planemanagement.service;

import com.example.planemanagement.model.Person;
import com.example.planemanagement.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlaneService {
    private static final int[] ROW_SIZES = {14, 12, 12, 14}; // A, B, C, D
    private static final int[][] seats = new int[4][]; // A, B, C, D
    private static final List<Ticket> tickets = new ArrayList<>();

    static {
        for (int i = 0; i < 4; i++) {
            seats[i] = new int[ROW_SIZES[i]];
            for (int j = 0; j < ROW_SIZES[i]; j++) {
                seats[i][j] = 0; // Initialize all seats as available
            }
        }
    }

    public Map<String, Object> buySeat(String row, int seat, Person person) {
        Map<String, Object> response = new HashMap<>();
        int rowNum = row.toUpperCase().charAt(0) - 'A';
        if (rowNum < 0 || rowNum >= 4) {
            response.put("message", "Invalid row letter.");
            response.put("success", false);
            return response;
        }
        int seatNum = seat - 1;
        if (seatNum < 0 || seatNum >= ROW_SIZES[rowNum]) {
            response.put("message", "Invalid seat number.");
            response.put("success", false);
            return response;
        }
        if (seats[rowNum][seatNum] == 1) {
            response.put("message", "Seat " + row + seat + " is already booked.");
            response.put("success", false);
            return response;
        }
        if (!person.getEmail().contains("@") || !person.getEmail().contains(".")) {
            response.put("message", "Invalid email address.");
            response.put("success", false);
            return response;
        }

        seats[rowNum][seatNum] = 1;
        int price = seatNum < 5 ? 200 : seatNum < 9 ? 150 : 180;
        tickets.add(new Ticket(row.charAt(0), seatNum, price, person));
        response.put("message", "Seat " + row + seat + " booked successfully.");
        response.put("success", true);
        return response;
    }

    public Map<String, Object> cancelSeat(String row, int seat) {
        Map<String, Object> response = new HashMap<>();
        int rowNum = row.toUpperCase().charAt(0) - 'A';
        if (rowNum < 0 || rowNum >= 4) {
            response.put("message", "Invalid row letter.");
            response.put("success", false);
            return response;
        }
        int seatNum = seat - 1;
        if (seatNum < 0 || seatNum >= ROW_SIZES[rowNum]) {
            response.put("message", "Invalid seat number.");
            response.put("success", false);
            return response;
        }
        if (seats[rowNum][seatNum] == 0) {
            response.put("message", "Seat " + row + seat + " is already available.");
            response.put("success", false);
            return response;
        }

        seats[rowNum][seatNum] = 0;
        tickets.removeIf(t -> t.getRow() == row.charAt(0) && t.getSeat() == seatNum);
        response.put("message", "Seat " + row + seat + " canceled successfully.");
        response.put("success", true);
        return response;
    }

    public Map<String, Object> findFirstAvailable() {
        Map<String, Object> response = new HashMap<>();
        char[] rows = {'A', 'B', 'C', 'D'};
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    response.put("message", "First available seat: Row " + rows[i] + ", Seat " + (j + 1));
                    response.put("success", true);
                    return response;
                }
            }
        }
        response.put("message", "No seats available.");
        response.put("success", false);
        return response;
    }

    public int[][] getSeatingPlan() {
        return seats;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Map<String, Object> searchTicket(String row, int seat) {
        Map<String, Object> response = new HashMap<>();
        int rowNum = row.toUpperCase().charAt(0) - 'A';
        if (rowNum < 0 || rowNum >= 4 || seat < 1 || seat > ROW_SIZES[rowNum]) {
            response.put("message", "Invalid row or seat number.");
            response.put("success", false);
            return response;
        }
        int seatNum = seat - 1;
        for (Ticket ticket : tickets) {
            if (ticket.getRow() == row.charAt(0) && ticket.getSeat() == seatNum) {
                response.put("message", "Ticket found: Row " + ticket.getRow() + ", Seat " + (ticket.getSeat() + 1) +
                        ", Price: £" + ticket.getPrice() + ", Name: " + ticket.getPerson().getName() +
                        ", Surname: " + ticket.getPerson().getSurname() + ", Email: " + ticket.getPerson().getEmail());
                response.put("success", true);
                response.put("ticket", ticket);
                return response;
            }
        }
        response.put("message", "Seat " + row + seat + " is available.");
        response.put("success", true);
        return response;
    }
}
