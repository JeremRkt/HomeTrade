package com.isep.hometrade.service;

import com.isep.hometrade.business.BookingEntity;
import com.isep.hometrade.business.HousingEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.dao.BookingRepository;
import com.isep.hometrade.map.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void saveBooking(BookingDto bookingDto, Date start, Date end, UserEntity userEntity, HousingEntity housingEntity) {
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setStart(start);
        bookingEntity.setEnd(end);
        bookingEntity.setAdults(Integer.parseInt(bookingDto.getAdults()));
        bookingEntity.setChildren(Integer.parseInt(bookingDto.getChildren()));
        bookingEntity.setStatus("Pending");
        bookingEntity.setUserEntity(userEntity);
        bookingEntity.setHousingEntity(housingEntity);
        bookingRepository.save(bookingEntity);
    }

    public Set<BookingEntity> findPendingBookingsBySuper(UserEntity userEntity) {
        Set<BookingEntity> bookingEntities = new HashSet<>();
        Set<HousingEntity> housingEntities = userEntity.getHousingEntities();
        for (HousingEntity housingEntity : housingEntities) {
            List<BookingEntity> bookingEntitiesByHousing = findPendingBookingsByHousing(housingEntity);
            bookingEntities.addAll(bookingEntitiesByHousing);
        }
        return bookingEntities;
    }

    public List<BookingEntity> findPendingBookingsByHousing(HousingEntity housingEntity) {
        return bookingRepository.findAllPendingByHousing(housingEntity);
    }

    public Set<BookingEntity> findAcceptedBookingsBySuper(UserEntity userEntity) {
        Set<BookingEntity> bookingEntities = new HashSet<>();
        Set<HousingEntity> housingEntities = userEntity.getHousingEntities();
        for (HousingEntity housingEntity : housingEntities) {
            List<BookingEntity> bookingEntitiesByHousing = findAcceptedBookingsByHousing(housingEntity);
            bookingEntities.addAll(bookingEntitiesByHousing);
        }
        return bookingEntities;
    }

    public List<BookingEntity> findAcceptedBookingsByHousing(HousingEntity housingEntity) {
        return bookingRepository.findAllAcceptedByHousing(housingEntity);
    }

    public Set<BookingEntity> findDeclinedBookingsBySuper(UserEntity userEntity) {
        Set<BookingEntity> bookingEntities = new HashSet<>();
        Set<HousingEntity> housingEntities = userEntity.getHousingEntities();
        for (HousingEntity housingEntity : housingEntities) {
            List<BookingEntity> bookingEntitiesByHousing = findDeclinedBookingsByHousing(housingEntity);
            bookingEntities.addAll(bookingEntitiesByHousing);
        }
        return bookingEntities;
    }

    public List<BookingEntity> findDeclinedBookingsByHousing(HousingEntity housingEntity) {
        return bookingRepository.findAllDeclinedByHousing(housingEntity);
    }

    public Set<BookingEntity> findPendingBookingsByUser(UserEntity userEntity) {
        List<BookingEntity> bookingEntities = bookingRepository.findAllPendingByUser(userEntity);
        return new HashSet<>(bookingEntities);
    }

    public Set<BookingEntity> findAcceptedBookingsByUser(UserEntity userEntity) {
        List<BookingEntity> bookingEntities = bookingRepository.findAllAcceptedByUser(userEntity);
        return new HashSet<>(bookingEntities);
    }

    public Set<BookingEntity> findDeclinedBookingsByUser(UserEntity userEntity) {
        List<BookingEntity> bookingEntities = bookingRepository.findAllDeclinedByUser(userEntity);
        return new HashSet<>(bookingEntities);
    }

    public void declineBooking(BookingEntity bookingEntity) {
        bookingEntity.setStatus("Declined");
        bookingRepository.save(bookingEntity);
    }

    public void acceptBooking(BookingEntity bookingEntity) {
        bookingEntity.setStatus("Accepted");
        bookingRepository.save(bookingEntity);
    }

    public BookingEntity findBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<BookingEntity> findBookingsByUser(UserEntity userEntity) {
        return bookingRepository.findByUser(userEntity);
    }

    public void deleteBookingsByUser(UserEntity userEntity) {
        List<BookingEntity> bookingEntities = findBookingsByUser(userEntity);
        bookingRepository.deleteAll(bookingEntities);
    }

    public boolean findAcceptedBookingByHousingAndUser(HousingEntity housingEntity, UserEntity userEntity) {
        BookingEntity bookingEntity = bookingRepository.findAcceptedByHousingAndUser(housingEntity, userEntity);
        return bookingEntity != null;
    }

    public boolean findPendingBookingByHousingAndUser(HousingEntity housingEntity, UserEntity userEntity) {
        BookingEntity bookingEntity = bookingRepository.findPendingByHousingAndUser(housingEntity, userEntity);
        return bookingEntity != null;
    }
}
