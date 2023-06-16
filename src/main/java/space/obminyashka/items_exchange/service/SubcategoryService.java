package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.model.Subcategory;
import java.util.List;
import java.util.Optional;

public interface SubcategoryService {

    /**
     * Returns all subcategory names by given Category ID.
     *
     * @param categoryId is Category ID.
     * @return list of subcategory names from DB that are represented as {@link String}
     */
    List<String> findSubcategoryNamesByCategoryId(long categoryId);

    /**
     * Removes the subcategory with the given ID from DB.
     *
     * @param subcategoryId is Subcategory ID to remove.
     */
    void removeSubcategoryById(long subcategoryId);

    /**
     * Retrieves a category by its ID.
     *
     * @param id is Subcategory ID
     * @return {@link Subcategory} entity from DB
     */
    Optional<Subcategory> findById(long id);

    /**
     * If a subcategory exists, returns {@code true}, otherwise {@code false}.
     *
     * @param id is Subcategory ID.
     * @return true if a subcategory with the given id exists, false otherwise.
     */
    boolean isSubcategoryExistsById(long id);

    /**
     * If subcategoryIds belong to categoryId, returns {@code true}, otherwise {@code false}.
     *
     * @param categoryId     is Category ID
     * @param subcategoryIds is List of Subcategory ID
     * @return true if subcategoryIds belong to categoryId
     */
    boolean checkSubcategoriesCategory(Long categoryId, List<Long> subcategoryIds);

    /**
     * Returns all Subcategory identifiers.
     *
     * @return list of all subcategory identifiers from DB
     */
    List<Long> findAllSubcategoryIds();
}
