package Models.Reservation;

public class ReservationJointureServices {
    private int idReservationPersonalise;
    private int idService;

    public ReservationJointureServices(int idReservationPersonalise, int idService) {
        this.idReservationPersonalise = idReservationPersonalise;
        this.idService = idService;
    }

    public ReservationJointureServices(int idReservationPersonalise) {
        this.idReservationPersonalise = idReservationPersonalise;
    }
    public ReservationJointureServices() {
            }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public int getIdReservationPersonalise() {
        return idReservationPersonalise;
    }

    public void setIdReservationPersonalise(int idReservationPersonalise) {
        this.idReservationPersonalise = idReservationPersonalise;
    }
}
