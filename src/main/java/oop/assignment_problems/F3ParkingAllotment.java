package oop.assignment_problems;

// F3. Object References, Null Safety, and a Mutating Method
// Null-safe parking slot allotment that can never throw NullPointerException.
public class F3ParkingAllotment {

    static class ParkingSlot {
        String slotNo;
        int capacity;
        int occupiedCount;

        ParkingSlot(String slotNo, int capacity, int occupiedCount) {
            this.slotNo = slotNo;
            this.capacity = capacity;
            this.occupiedCount = occupiedCount;
        }

        boolean allot(String vehicleNo) {
            if (occupiedCount < capacity) {
                occupiedCount++;
                System.out.println(vehicleNo + " allotted to slot " + slotNo);
                return true;
            }
            return false;
        }

        static ParkingSlot findAvailableSlot(ParkingSlot[] slots) {
            for (ParkingSlot s : slots) {
                if (s.occupiedCount < s.capacity) {
                    return s;
                }
            }
            return null;
        }

        static void safeAllot(ParkingSlot[] slots, String vehicleNo) {
            ParkingSlot free = findAvailableSlot(slots);
            if (free == null) {
                System.out.println("No slots available for " + vehicleNo);
            } else {
                free.allot(vehicleNo);
            }
        }
    }

    // Passing the ParkingSlot array does NOT copy the slots: Java passes the
    // array reference by value, so the method operates on the same slot objects
    // on the heap. Mutating occupiedCount inside is visible to the caller.
    public static void main(String[] args) {
        ParkingSlot[] withSpace = {
            new ParkingSlot("A1", 4, 3),
            new ParkingSlot("A2", 5, 5)
        };
        ParkingSlot.safeAllot(withSpace, "TN09AB1234");

        ParkingSlot[] allFull = {
            new ParkingSlot("A1", 4, 4),
            new ParkingSlot("A2", 5, 5)
        };
        ParkingSlot.safeAllot(allFull, "TN09AB1234");
    }
}
