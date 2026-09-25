package pe.edu.vallegrande.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.Backend.model.ReservationDetail;

import java.util.List;

@Repository
public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Long> {
    List<ReservationDetail> findByIdReservation(Long idReservation);
}