import com.mypersonalupdates.Filter;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.filters.PartialAttributeFilter;
import com.mypersonalupdates.log.Log;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import com.mypersonalupdates.providers.twitter.TwitterProvider;
import com.mypersonalupdates.users.User;

import java.util.Collection;

/* ATENCIÓN!!
*
* Antes de ejecutar este test:
* - Verificar que el archivo de configuración "config.conf" posee todos los datos
*     actualizados, en particular el ID de proveedor de Twitter y los IDs de los
*     atributos así como también sus correctas categorías.
*
* */

public class TwitterProvider_Log_Test {
    public static void main(String[] args) {
        UpdatesProvider provider = TwitterProvider.getInstance();

        User nahuelUser = null;
        try {
            nahuelUser = User.getFromUsername("nahuel");
            if (nahuelUser == null) {
                nahuelUser = User.createNew("nahuel", "password");

                assert nahuelUser != null;

                nahuelUser.setAttribute(
                        provider,
                        "providers.twitter.token",
                        "98758588-pRnp7Qndq4faaMaNXRFDctD3acg56YL0Dugpu4VEf"
                );
                nahuelUser.setAttribute(
                        provider,
                        "providers.twitter.tokenSecret",
                        "68iikmgVPgyzhl3tjP2kSKzuW2g0R1CVKXuIHPUYDpvqc"
                );
            }
        } catch (DBException e) {
            e.printStackTrace();
        }

        System.out.println(provider.getID());
        System.out.println(provider.getName());
        System.out.println(provider.getDescription());

        Collection<UpdatesProviderAttribute> attributes = provider.getAttributes();

        assert attributes != null;

        UpdatesProviderAttribute attr1 = null;

        System.out.println("Attributes:");
        for (UpdatesProviderAttribute attribute : attributes) {
            if (attribute.getAttrID() == 1)
                attr1 = attribute;
            System.out.println("\t- " + attribute.getAttrID());
        }

        assert attr1 != null;

        Filter filter = null;

        try {
            filter = PartialAttributeFilter.create(attr1, "#7YearsOfOneDirection");
        } catch (DBException e) {
            e.printStackTrace();
        }

        assert filter != null;

        filter.getValues(attr1).forEach(
                value -> System.out.println(value.getValue())
        );

        UpdatesProviderAttribute finalAttr = attr1;
/*        Long id = provider.subscribe(
                nahuelUser,
                filter,
                update -> {
                    Collection<String> values = update.getAttributeValues(finalAttr);

                    if (values == null) {
                        System.out.println("MAIN: EMPTY TWEET");
                        return;
                    }

                    Iterator<String> iterator = values.iterator();

                    if (!iterator.hasNext()) {
                        System.out.println("MAIN: EMPTY TWEET");
                        return;
                    }

                    System.out.println("MAIN (" + values.size() + "):" + iterator.next());
                }
        );

        assert id != null;*/

        Long id = provider.subscribe(
                nahuelUser,
                filter,
                Log.getInstance()
        );

        assert id != null;
    }
}
