package org.example.game;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


/**
 * TODO There are some tasks e.g. Ruslan laundry, that cannot be scheduled in advance and are rather
 * on demand tasks. These could either be "assigned" to someone or have its own category. These type of tasks
 * help balance KARMA e.g. if Andres has negative karma relative to Alex, and if ruslan laundry is "called-out", then
 * Andres will be "signaled" to pick up that task first.
 */
@Builder
public class Mission {

    public enum MissionState {
        CREATED,
        STARTED,
        DONE
    }

    String name;
    Life reward;

    Schedule schedule;

    LocalDate scheduledAt;
    LocalDate dueAt;
    LocalDate completedAt;

    MissionTemplate template;

    // List of citizens that should complete this mission (not enforced).
    // This is important at assignment, when the `World` determines who
    // it can assign this mission to.
    List<Citizen> authorizedCrew;

    Citizen owner;

    @Default
    @Setter
    @Getter
    private MissionState state = MissionState.CREATED;

   public static class MissionBuilder {
       public MissionBuilder owner(Citizen owner) throws Exception {
           throw new Exception("use assignTo instead");
       }
   }

   public void markComplete() {
       if (state == MissionState.DONE) return;

       state = MissionState.DONE;
       completedAt = LocalDate.now();
   }

    public boolean complete() {
       if (state == MissionState.DONE) return true;

       if (owner != null) {
            try {
                owner.completeMission(this, 1);
            } catch (Exception ex) {
                System.out.printf("could not award mission %s to owner %s%n", this, owner);
                return false;
            }
       }

        markComplete();
        return true;
    }

    public boolean assignTo(Citizen citizen) {
        owner = citizen;
        if (!citizen.alreadyAssigned(this)) citizen.assignMission(this);
        return true;
    }

    public long daysLeft() {
        var now = java.time.LocalDate.now();
        return now.until(scheduledAt, ChronoUnit.DAYS);
    }

    public boolean isOverdue() {
        return !state.equals(MissionState.DONE) && LocalDate.now().isAfter(dueAt);
    }
}
