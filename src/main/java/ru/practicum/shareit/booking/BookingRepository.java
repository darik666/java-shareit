package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id = ?1 " +
            "AND b.booker.id = ?2 " +
            "AND b.end < current_timestamp")
    List<Booking> findByItemIdAndOwnerId(Long itemId, Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllBookings(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.start <= current_timestamp AND b.end >= current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookings(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.end <= current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookings(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.start >= current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureBookings(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'WAITING' " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingsByWaiting(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingsByRejected(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerAllBookings(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.start <= current_timestamp AND b.end >= current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerCurrentBookings(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.end <= current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerPastBookings(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.start >= current_timestamp " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerFutureBookings(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'WAITING' " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerBookingsByWaiting(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerBookingsByRejected(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.id = ?1 " +
            "AND i.owner.id = ?2 " +
            "AND b.start < current_timestamp " +
            "AND b.status NOT LIKE 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findLastBookingForItem(Long itemId, Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.id = ?1 " +
            "AND i.owner.id = ?2 " +
            "AND b.start > current_timestamp " +
            "AND b.status NOT LIKE 'REJECTED' " +
            "ORDER BY b.start ASC")
    List<Booking> findNextBookingForItem(Long itemId, Long ownerId);
}