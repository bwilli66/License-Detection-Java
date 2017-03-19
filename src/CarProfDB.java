/**
 * Created by BradWilliams on 1/11/17.
 */
public interface CarProfDB {
    CarProf getCarProf(String licensePlate);
    void addCarProf(CarProf carProf);
}
