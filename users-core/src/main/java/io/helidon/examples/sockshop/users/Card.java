package io.helidon.examples.sockshop.users;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Representation of a credit card.
 */
@Data
@NoArgsConstructor
@Entity
@IdClass(CardId.class)
@Schema(description = "User credit card")
public class Card implements Serializable {
    /**
     * The card identifier.
     */
    @Id
    @Schema(description = "The card identifier")
    private String cardId;

    /**
     * The card number.
     */
    @Schema(description = "The card number")
    private String longNum;

    /**
     * The expiration date.
     */
    @Schema(description = "The card expiration date")
    private String expires;

    /**
     * The security code.
     */
    @Schema(description = "The card security code")
    private String ccv;

    /**
     * The user this card belongs to, purely for JPA optimization.
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonbTransient
    private User user;

    /**
     * Construct {@code Card} with specified parameters.
     */
    public Card(String longNum, String expires, String ccv) {
        this.longNum = longNum;
        this.expires = expires;
        this.ccv = ccv;
    }

    /**
     * Return the user this address belongs to.
     *
     * @return the user this address belongs to
     */
    User getUser() {
    return user;
    }

    /**
     * Set the uer this address belongs to.
     *
     * @param user the user to set
     *
     * @return this card
     */
    Card setUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * Set the card id.
     */
    Card setCardId(String id) {
        this.cardId = id;
        return this;
    }

    /**
     * Return CardId for this card.
     */
    public CardId getId() {
        return new CardId(user.getUsername(), cardId);
    }

    /**
     * Return the card with masked card number.
     *
     * @return the card with masked card number
     */
    public Card mask() {
        if (longNum != null) {
            int len = longNum.length() - 4;
            longNum = "****************".substring(0, len) + longNum.substring(len);
        }
        return this;
    }

    /**
     * Return the last 4 digit of the card number.
     *
     * @return the last 4 digit of the card number
     */
    public String last4() {
        return longNum == null ? null : longNum.substring(longNum.length() - 4);
    }

    @JsonbProperty("_links")
    public Links getLinks() {
        CardId id = getId();
        return id != null
            ? Links.card(id.toString())
            : Links.card("");
    }
}
