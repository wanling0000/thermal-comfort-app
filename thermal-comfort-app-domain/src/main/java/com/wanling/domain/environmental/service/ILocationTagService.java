package com.wanling.domain.environmental.service;

import java.util.Optional;

import com.wanling.domain.environmental.model.entity.LocationTagEntity;
import com.wanling.domain.environmental.model.valobj.LocationCandidateVO;

public interface ILocationTagService {

    /**
     * Automatically normalize a location candidate.
     *
     * If a tag with the same display name exists within a certain radius (e.g. 50 meters),
     * return its location_tag_id. Otherwise, create a new location tag and return its ID.
     *
     * This method is typically used for sensor-based readings without explicit user input.
     *
     * @param candidate The incoming location data (from sensors or frontend).
     * @return The ID of the matched or newly created location tag.
     */
    String findOrCreate(LocationCandidateVO candidate);


    /**
     * Check if a normalized location tag already exists based on the given candidate.
     *
     * Does not create any new tag — only performs spatial + name matching.
     *
     * Useful for frontend previews, suggestions, or idempotent checks.
     *
     * @param candidate The location candidate to search for.
     * @return An optional existing location tag.
     */
    Optional<LocationTagEntity> findExisting(LocationCandidateVO candidate);

//    /**
//     * Resolve the correct location_tag_id for a given location candidate.
//     *
//     * This is the preferred entry point for any domain logic that needs to
//     * associate a location with an entity (e.g. reading, feedback).
//     *
//     * - If the location is marked as custom and has a customTag ID,
//     *   this will resolve its mapped system tag (via UserLocationService).
//     * - Otherwise, it will attempt to find or create a tag using findOrCreate().
//     *
//     * @param candidate The location data, possibly from the frontend.
//     * @return The resolved location_tag_id.
//     */
//    String resolveLocationTag(LocationCandidateVO candidate);
}