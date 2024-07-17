package com.ribaso.checkoutservice.core.domain.service.interfaces;

import com.ribaso.checkoutservice.core.domain.model.Checkout;
import com.ribaso.checkoutservice.port.exception.CheckoutAlreadyCompletedException;
import com.ribaso.checkoutservice.port.exception.InvalidCheckoutIdException;


/**
 * Interface zur Definition der Dienste für Checkout-Prozesse.
 */
public interface CheckoutService {

    /**
     * Initiiert einen neuen Checkout-Prozess und erstellt eine neue Checkout-Instanz in der Datenbank.
     * Der Status des Checkouts wird auf "pending" gesetzt.
     *
     * @return Das erstellte Checkout-Objekt mit Status 'pending'.
     */
    Checkout initiateCheckout(String userId);

    /**
     * Schließt einen Checkout-Prozess ab, indem der Status auf "completed" gesetzt wird.
     * Wirft InvalidCheckoutIdException, wenn die Checkout-ID ungültig ist.
     * Wirft CheckoutAlreadyCompletedException, wenn der Checkout bereits abgeschlossen wurde.
     *
     * @param checkoutId Die ID des Checkouts, der abgeschlossen werden soll.
     * @return Das aktualisierte Checkout-Objekt mit Status 'completed'.
     * @throws InvalidCheckoutIdException Falls keine gültige Checkout-ID bereitgestellt wird.
     * @throws CheckoutAlreadyCompletedException Wenn der Checkout bereits abgeschlossen ist.
     */
    Checkout completeCheckout(Long checkoutId) throws InvalidCheckoutIdException, CheckoutAlreadyCompletedException;
}
