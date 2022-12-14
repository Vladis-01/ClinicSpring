package com.example.clinicspring.spring.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicineDto implements Serializable {

    private long id;

    private AppointmentDto appointmentDto;

    @JsonProperty(value = "packing_id")
    private long packingId;

    @JsonProperty(value = "trade_name_rus")
    private String name = "Не найдено";

    @JsonProperty(value = "dose")
    private String dose = "Не найдено";

    @JsonProperty(value = "dosage_form_full_name")
    private String dosageForm = "Не найдено";

    @JsonProperty(value = "amount1")
    private String amount = "Не найдено";

    @JsonProperty(value = "producer_tran")
    private String firm = "Не найдено";

    @JsonProperty(value = "producer_country")
    private String country = "Не найдено";

    @JsonProperty(value = "lt_month")
    private String ltMonth = "Не найдено"; // Срок хранения

    @JsonProperty(value = "packing_full")
    private String packingFull = "Не найдено";

    @JsonProperty(value = "sc_name")
    private String sc = "Не найдено";

    @JsonProperty(value = "reg_date")
    private String regDate = "Не найдено";

    @JsonProperty(value = "ntfr_name")
    private String type = "Не найдено";

    public void setValueId(Long s) {
        if (s != null) {
            id = s;
        }
    }

    @JsonSetter("packing_id")
    public void setValuePackingId(Long s) {
        if (s != null) {
            packingId = s;
        }
    }

    @JsonSetter("some-trade_name_rus")
    public void setValueName(String s) {
        if (s != null) {
            name = s;
        }
    }

    @JsonSetter("dose")
    public void setValueDose(String s) {
        if (s != null) {
            dose = s;
        }
    }

    @JsonSetter("dosage_form_full_name")
    public void setValueDosageForm(String s) {
        if (s != null) {
            dosageForm = s;
        }
    }

    @JsonSetter("amount1")
    public void setValueAmount(String s) {
        if (s != null) {
            amount = s;
        }
    }

    @JsonSetter("producer_tran")
    public void setValueFirm(String s) {
        if (s != null) {
            firm = s;
        }
    }

    @JsonSetter("producer_country")
    public void setValueCountry(String s) {
        if (s != null) {
            country = s;
        }
    }

    @JsonSetter("lt_month")
    public void setValueLtMonth(String s) {
        if (s != null) {
            ltMonth = s;
        }
    }
    @JsonSetter("packing_full")
    public void setValuePackingFull(String s) {
        if (s != null) {
            packingFull = s;
        }
    }

    @JsonSetter("sc_name")
    public void setValueSc(String s) {
        if (s != null) {
            sc = s;
        }
    }
    @JsonSetter("reg_date")
    public void setValueRegDate(String s) {
        if (s != null) {
            regDate = s;
        }
    }

    @JsonSetter("ntfr_name")
    public void setValueType(String s) {
        if (s != null) {
            type = s;
        }
    }

    public void setValueAppointment(AppointmentDto appointmentDto) {
        this.appointmentDto = appointmentDto;
    }
}
