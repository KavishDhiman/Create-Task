package com.createtask.createtask.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "TaskCategory")
public class TaskCategory {

    @EmbeddedId
    private TaskCategoryId id;

    @ManyToOne
    @MapsId("taskID")
    @JoinColumn(name = "TaskID")
    private Task task;

    @ManyToOne
    @MapsId("categoryID")
    @JoinColumn(name = "CategoryID")
    private Category category;

    @Embeddable
    @Getter
    @Setter
    public static class TaskCategoryId implements Serializable {

        @Column(name = "TaskID")
        private int taskID;

        @Column(name = "CategoryID")
        private int categoryID;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TaskCategoryId)) return false;
            TaskCategoryId that = (TaskCategoryId) o;
            return taskID == that.taskID && categoryID == that.categoryID;
        }

        @Override
        public int hashCode() { return Objects.hash(taskID, categoryID); }
    }
}
