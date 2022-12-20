package workplaceManager.db.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Accounting1C implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String inventoryNumber;

    @Column
    private String title;

    @Column
    private Boolean deleted = false;

    @ManyToOne(optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee")
    private Employee employee;

    @OneToMany(mappedBy = "accounting1C", fetch = FetchType.LAZY)
    private List<Equipment> equipmentList = new ArrayList<>();

    public String getTitleReplace() {
        return title
                .replaceAll("&", "&amp;")
                .replaceAll("\"","&quot;") // Двойная кавычка
                .replaceAll("<", "&lt;")   // Открывающаяся угловая скобка
                .replaceAll(">", "&gt;");  // Закрывающаяся угловая скобка
                /*.replaceAll("%", "%25")   // Процент
                .replaceAll(" ", "%20")   // Пробел
                .replaceAll("\t", "%20")  // Табуляция (заменяем на пробел)
                .replaceAll("\n", "%20")  // Переход строки (заменяем на пробел)
                .replaceAll("\r", "%20")  // Возврат каретки (заменяем на пробел)
                .replaceAll("!", "%21")   // Восклицательный знак
                .replaceAll("\"", "%22")  // Двойная кавычка
                .replaceAll("#", "%23")   // Октоторп, решетка
                .replaceAll("\\$", "%24") // Знак доллара
                .replaceAll("&", "%26")   // Амперсанд
                .replaceAll("'", "%27")   // Одиночная кавычка
                .replaceAll("\\(", "%28") // Открывающаяся скобка
                .replaceAll("\\)", "%29") // Закрывающаяся скобка
                .replaceAll("\\*", "%2a") // Звездочка
                .replaceAll("\\+", "%2b") // Знак плюс
                .replaceAll(",", "%2c")   // Запятая
                .replaceAll("-", "%2d")   // Дефис
                .replaceAll("\\.", "%2e") // Точка
                .replaceAll("/", "%2f")   // Слеш, косая черта
                .replaceAll(":", "%3a")   // Двоеточие
                .replaceAll(";", "%3b")   // Точка с запятой
                .replaceAll("<", "%3c")   // Открывающаяся угловая скобка
                .replaceAll("=", "%3d")   // Знак равно
                .replaceAll(">", "%3e")   // Закрывающаяся угловая скобка
                .replaceAll("\\?", "%3f") // Вопросительный знак
                .replaceAll("@", "%40")   // At sign, по цене, собачка
                .replaceAll("\\[", "%5b") // Открывающаяся квадратная скобка
                .replaceAll("\\\\", "%5c") // Одиночный обратный слеш '\'
                .replaceAll("\\]", "%5d") // Закрывающаяся квадратная скобка
                .replaceAll("\\^", "%5e") // Циркумфлекс
                .replaceAll("_", "%5f")   // Нижнее подчеркивание
                .replaceAll("`", "%60")   // Гравис
                .replaceAll("\\{", "%7b") // Открывающаяся фигурная скобка
                .replaceAll("\\|", "%7c") // Вертикальная черта
                .replaceAll("\\}", "%7d") // Закрывающаяся фигурная скобка
                .replaceAll("~", "%7e");  // Тильда*/
    }

    public Accounting1C() {

    }

    public Accounting1C(String inventoryNumber, String title, Employee employee) {
        this.inventoryNumber = inventoryNumber;
        this.title = title;
        this.employee = employee;
    }

    @Override
    public String toString() {
        String str = String.format("%s (%s, %s)", title, employee == null ? "" : employee.getName(), inventoryNumber);
        if (deleted) {
            str += " (списано. id=" + id + ")";
        }
        return str;
    }

    @Transient
    public static boolean equalsAccounting1C(Accounting1C accounting1C1, Accounting1C accounting1C2) {
        if (accounting1C1 == null && accounting1C2 == null) {
            return true;
        }
        if ((accounting1C1 == null && accounting1C2 != null) ||
                (accounting1C1 != null && accounting1C2 == null)) {
            return false;
        }
        if (accounting1C1.getId() == accounting1C2.getId() &&
                StringUtils.equals(accounting1C1.getTitle(), accounting1C2.getTitle()) &&
                StringUtils.equals(accounting1C1.getInventoryNumber(), accounting1C2.getInventoryNumber())) {
            return true;
        }
        return false;
    }
}
