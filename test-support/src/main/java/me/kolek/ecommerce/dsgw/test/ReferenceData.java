package me.kolek.ecommerce.dsgw.test;

import me.kolek.ecommerce.dsgw.model.Carrier;
import me.kolek.ecommerce.dsgw.model.OrderCancelCode;
import me.kolek.ecommerce.dsgw.model.ServiceLevel;

public class ReferenceData {
  public static final class Carriers {

    private static final Carrier FEDEX = Carrier.builder()
        .name("FedEx")
        .build();

    public static Carrier fedex() {
      return FEDEX;
    }
  }

  public static final class ServiceLevels {
    private static final ServiceLevel FEDEX_HOME_DELIVERY = ServiceLevel.builder()
        .carrier(Carriers.fedex())
        .mode("Home Delivery")
        .code("FDX.HD")
        .build();
    private static final ServiceLevel FEDEX_GROUND = ServiceLevel.builder()
        .carrier(Carriers.fedex())
        .mode("Ground")
        .code("FDX.GRND")
        .build();
    private static final ServiceLevel FEDEX_2_DAY = ServiceLevel.builder()
        .carrier(Carriers.fedex())
        .mode("2 Day")
        .code("FDX.2D")
        .build();

    public static ServiceLevel fedexHomeDelivery() {
      return FEDEX_HOME_DELIVERY;
    }

    public static ServiceLevel fedexGround() {
      return FEDEX_GROUND;
    }

    public static ServiceLevel fedex2Day() {
      return FEDEX_2_DAY;
    }

    public static ServiceLevel[] values() {
      return new ServiceLevel[]{FEDEX_HOME_DELIVERY, FEDEX_GROUND, FEDEX_2_DAY};
    }
  }

  public static class OrderCancelCodes {
    private static final OrderCancelCode OTHER = OrderCancelCode.builder()
        .code("CXRO")
        .description("Other/Unknown")
        .build();

    public static OrderCancelCode other() {
      return OTHER;
    }

    public static OrderCancelCode[] values() {
      return new OrderCancelCode[]{OTHER};
    }
  }
}
