package com.awesome.pizza.brick.repository;

import com.awesome.pizza.brick.entity.Order;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/** Repository for managing Order entities in Awesome Pizza. */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  // Find an order by its unique code
  Order findByCode(String code);

  /**
   * * Find all orders matching the given statuses and pickup date.
   *
   * @param statuses List of order statuses to filter by (optional).
   * @param pickupDate Date to filter orders by user's pickup window (optional).
   * @return List of orders matching the criteria.
   */
  @Query(
      value =
          "SELECT o.* FROM orders o JOIN users u ON o.user_id = u.id "
              + "WHERE (:statuses IS NULL OR o.status IN (:statuses)) "
              + "AND (:pickupDate IS NULL OR (CAST(u.pickup_from AS DATE) >= :pickupDate AND CAST(u.pickup_to AS DATE) <= :pickupDate))",
      nativeQuery = true)
  List<Order> findAllByStatusAndPickupDate(
      @Nullable @Param("statuses") List<String> statuses,
      @Nullable @Param("pickupDate") LocalDate pickupDate);
}
