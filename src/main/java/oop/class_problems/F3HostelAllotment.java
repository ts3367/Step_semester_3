package oop.class_problems;

// F3. Object References, Null Safety, and a Mutating Method
// Null-safe hostel room allotment that can never throw NullPointerException.
public class F3HostelAllotment {

    static class HostelRoom {
        String roomNo;
        int beds;
        int occupied;

        HostelRoom(String roomNo, int beds, int occupied) {
            this.roomNo = roomNo;
            this.beds = beds;
            this.occupied = occupied;
        }

        boolean allot(String name) {
            if (occupied < beds) {
                occupied++;
                System.out.println(name + " allotted to room " + roomNo);
                return true;
            }
            return false;
        }

        static HostelRoom findAvailableRoom(HostelRoom[] rooms) {
            for (HostelRoom r : rooms) {
                if (r.occupied < r.beds) {
                    return r;
                }
            }
            return null;
        }

        static void safeAllot(HostelRoom[] rooms, String studentName) {
            HostelRoom free = findAvailableRoom(rooms);
            if (free == null) {
                System.out.println("No rooms available for " + studentName);
            } else {
                free.allot(studentName);
            }
        }
    }

    // Passing the HostelRoom array does NOT copy the rooms: Java passes the
    // array reference by value, so the method sees the very same HostelRoom
    // objects on the heap. Mutations like occupied++ are visible to the caller.
    public static void main(String[] args) {
        HostelRoom[] withSpace = {
            new HostelRoom("C-214", 3, 2),
            new HostelRoom("C-507", 2, 2)
        };
        HostelRoom.safeAllot(withSpace, "Divya");

        HostelRoom[] allFull = {
            new HostelRoom("C-214", 3, 3),
            new HostelRoom("C-507", 2, 2)
        };
        HostelRoom.safeAllot(allFull, "Divya");
    }
}
