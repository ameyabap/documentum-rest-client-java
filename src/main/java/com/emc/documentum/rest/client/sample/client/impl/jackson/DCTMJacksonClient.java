/*
 * Copyright (c) 2016. EMC Corporation. All Rights Reserved.
 */
package com.emc.documentum.rest.client.sample.client.impl.jackson;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.emc.documentum.rest.client.sample.client.DCTMRestClient;
import com.emc.documentum.rest.client.sample.client.impl.AbstractRestTemplateClient;
import com.emc.documentum.rest.client.sample.client.util.Headers;
import com.emc.documentum.rest.client.sample.client.util.UriHelper;
import com.emc.documentum.rest.client.sample.model.Comment;
import com.emc.documentum.rest.client.sample.model.Entry;
import com.emc.documentum.rest.client.sample.model.Feed;
import com.emc.documentum.rest.client.sample.model.FolderLink;
import com.emc.documentum.rest.client.sample.model.HomeDocument;
import com.emc.documentum.rest.client.sample.model.LinkRelation;
import com.emc.documentum.rest.client.sample.model.Linkable;
import com.emc.documentum.rest.client.sample.model.ObjectAspects;
import com.emc.documentum.rest.client.sample.model.Permission;
import com.emc.documentum.rest.client.sample.model.PermissionSet;
import com.emc.documentum.rest.client.sample.model.Preference;
import com.emc.documentum.rest.client.sample.model.Repository;
import com.emc.documentum.rest.client.sample.model.RestObject;
import com.emc.documentum.rest.client.sample.model.RestType;
import com.emc.documentum.rest.client.sample.model.SavedSearch;
import com.emc.documentum.rest.client.sample.model.Search;
import com.emc.documentum.rest.client.sample.model.SearchFeed;
import com.emc.documentum.rest.client.sample.model.SearchTemplate;
import com.emc.documentum.rest.client.sample.model.ValueAssistant;
import com.emc.documentum.rest.client.sample.model.ValueAssistantRequest;
import com.emc.documentum.rest.client.sample.model.VirtualDocumentNode;
import com.emc.documentum.rest.client.sample.model.batch.Batch;
import com.emc.documentum.rest.client.sample.model.batch.Capabilities;
import com.emc.documentum.rest.client.sample.model.json.JsonBatch;
import com.emc.documentum.rest.client.sample.model.json.JsonBatchCapabilities;
import com.emc.documentum.rest.client.sample.model.json.JsonComment;
import com.emc.documentum.rest.client.sample.model.json.JsonFeeds;
import com.emc.documentum.rest.client.sample.model.json.JsonFolderLink;
import com.emc.documentum.rest.client.sample.model.json.JsonHomeDocument;
import com.emc.documentum.rest.client.sample.model.json.JsonObject;
import com.emc.documentum.rest.client.sample.model.json.JsonObjectAspects;
import com.emc.documentum.rest.client.sample.model.json.JsonPermission;
import com.emc.documentum.rest.client.sample.model.json.JsonPermissionSet;
import com.emc.documentum.rest.client.sample.model.json.JsonPreference;
import com.emc.documentum.rest.client.sample.model.json.JsonRepository;
import com.emc.documentum.rest.client.sample.model.json.JsonSavedSearch;
import com.emc.documentum.rest.client.sample.model.json.JsonSearchTemplate;
import com.emc.documentum.rest.client.sample.model.json.JsonType;
import com.emc.documentum.rest.client.sample.model.json.JsonValueAssistance;
import com.emc.documentum.rest.client.sample.model.json.JsonValueAssistantRequest;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.emc.documentum.rest.client.sample.model.LinkRelation.ABOUT;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.ACLS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.ASPECT_TYPES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.ASSIS_VALUES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.ASSOCIATIONS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.BATCH_CAPABILITIES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.CABINETS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.CANCEL_CHECKOUT;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.CHECKIN_BRANCH_VERSION;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.CHECKIN_NEXT_MAJOR;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.CHECKIN_NEXT_MINOR;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.CHECKOUT;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.COMMENTS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.CONTENTS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.CURRENT_USER_PREFERENCES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.DELETE;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.DEMATERIALIZE;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.DOCUMENTS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.EDIT;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.FOLDERS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.FORMATS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.GROUPS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.MATERIALIZE;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.NETWORK_LOCATIONS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.OBJECTS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.OBJECT_ASPECTS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.PAGING_FIRST;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.PAGING_LAST;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.PAGING_NEXT;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.PAGING_PREV;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.PRIMARY_CONTENT;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.RELATIONS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.RELATION_TYPES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.REPLIES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.REPOSITORIES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.SAVED_SEARCHES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.SAVED_SEARCH_SAVED_RESULTS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.SEARCH;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.SEARCH_EXECUTION;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.SEARCH_TEMPLATES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.SELF;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.SHARED_PARENT;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.TYPES;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.USERS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.VERSIONS;
import static com.emc.documentum.rest.client.sample.model.LinkRelation.VIRTUAL_DOCUMENT_NODES;

/**
 * the DCTMRestClient implementation by Jackson json support
 */
@NotThreadSafe
public class DCTMJacksonClient extends AbstractRestTemplateClient implements DCTMRestClient {
    public DCTMJacksonClient(String contextRoot, String repositoryName,
            String username, String password, boolean useFormatExtension) {
        super(contextRoot, repositoryName, username, password, useFormatExtension);
    }
    
    @Override
    public HomeDocument getHomeDocument() {
        if(homeDocument == null) {
            homeDocument = get(getHomeDocumentUri(), Headers.ACCEPT_JSON_HOME_DOCUMENT, JsonHomeDocument.class);
        }
        return homeDocument;
    }
    
    @Override
    public RestObject getProductInfo() {
        if(productInfo == null) {
            productInfo = get(getHomeDocument().getHref(ABOUT), false, JsonObject.class);
        }
        return productInfo;
    }

    @Override
    public Feed<Repository> getRepositories() {
        if(repositories == null) {
            String repositoriesUri = getHomeDocument().getHref(REPOSITORIES);
            Feed<? extends Repository> feed = get(repositoriesUri, true, JsonFeeds.RepositoryFeed.class);
            repositories = (Feed<Repository>)feed;
        }
        return repositories;
    }
    
    @Override
    public Repository getRepository() {
        if(repository == null) {
            boolean resetEnableStreaming = enableStreaming;
            Feed<Repository> repositories = getRepositories();
            for(Entry<Repository> e : repositories.getEntries()) {
                if(repositoryName.equals(e.getTitle())) {
                    repository = get(e.getContentSrc(), false, JsonRepository.class);
                }
            }
            if(resetEnableStreaming) {
                enableStreaming = resetEnableStreaming;
            }
        }
        return repository;
    }
    
    @Override
    public Feed<RestObject> dql(String dql, String... params) {
        Feed<? extends RestObject> feed = get(getRepository().getHref(SELF), true, JsonFeeds.ObjectFeed.class, UriHelper.append(params, "dql", dql));
        return (Feed<RestObject>)feed;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public SearchFeed<RestObject> search(String search, String... params) {
        SearchFeed<? extends RestObject> feed = get(getRepository().getHref(SEARCH), true, JsonFeeds.SearchFeed.class, UriHelper.append(params, "q", search));
        return (SearchFeed<RestObject>)feed;
    }

    @Override
    public SearchFeed<RestObject> search(Search search, String... params) {
        SearchFeed<? extends RestObject> feed = post(getRepository().getHref(SEARCH), search, JsonFeeds.SearchFeed.class, params);
        return (SearchFeed<RestObject>)feed;
    }

    @Override
    public Feed<RestObject> getCabinets(String... params) {
        Repository repository = getRepository();
        String cabinetsUri = repository.getHref(CABINETS);
        Feed<? extends RestObject> feed = get(cabinetsUri, true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject getCabinet(String cabinet, String... params) {
        RestObject obj = null; 
        Feed<RestObject> feed = getCabinets("filter", "starts-with(object_name,'" + cabinet + "')");
        List<Entry<RestObject>> entries = feed.getEntries();
        if(entries != null) {
            for(Entry<RestObject> e : (List<Entry<RestObject>>)entries) {
                if(cabinet.equals(e.getTitle())) {
                    obj = get(e.getContentSrc(), false, JsonObject.class);
                    break;
                }
            }
        }
        return obj;
    }
    
    @Override
    public RestObject get(RestObject object, String... params) {
        return get(object.getHref(SELF), false, object.getClass(), params);
    }
    
    @Override
    public Feed<RestObject> getFolders(Linkable parent, String... params) {
        Feed<? extends RestObject> feed = get(parent.getHref(FOLDERS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public Feed<RestObject> getObjects(Linkable parent, String... params) {
        Feed<? extends RestObject> feed = get(parent.getHref(OBJECTS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public Feed<RestObject> getDocuments(Linkable parent, String... params) {
        Feed<? extends RestObject> feed = get(parent.getHref(DOCUMENTS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject createFolder(Linkable parent, RestObject newFolder, String... params) {
        return post(parent.getHref(FOLDERS), new JsonObject(newFolder), JsonObject.class, params);
    }
    
    @Override
    public RestObject getFolder(String folderUri, String... params) {
        return get(folderUri, false, JsonObject.class, params);
    }
    
    @Override
    public RestObject createObject(Linkable parent, LinkRelation rel, RestObject objectToCreate, Object content, String contentMediaType, String... params) {
        return post(parent.getHref(rel), new JsonObject(objectToCreate), content, contentMediaType, JsonObject.class, params);
    }

    @Override
    public RestObject createObject(Linkable parent, LinkRelation rel, RestObject objectToCreate, List<Object> contents, List<String> contentMediaTypes, String... params) {
        return post(parent.getHref(rel), new JsonObject(objectToCreate), contents, contentMediaTypes, JsonObject.class, params);
    }

    @Override
    public RestObject getObject(String objectUri, String... params) {
        return get(objectUri, false, JsonObject.class, params);
    }
    
    @Override
    public RestObject createDocument(Linkable parent, RestObject objectToCreate, Object content, String contentMediaType, String... params) {
        return post(parent.getHref(DOCUMENTS), new JsonObject(objectToCreate), content, contentMediaType, JsonObject.class, params);
    }
    
    @Override
    public RestObject createDocument(Linkable parent, RestObject objectToCreate, List<Object> contents, List<String> contentMediaTypes, String... params) {
        return post(parent.getHref(DOCUMENTS), new JsonObject(objectToCreate), contents, contentMediaTypes, JsonObject.class, params);
    }
    
    @Override
    public RestObject getDocument(String documentUri, String... params) {
        return get(documentUri, false, JsonObject.class, params);
    }
    
    @Override
    public RestObject update(RestObject oldObject, RestObject newObject, String... params) {
        return update(oldObject, EDIT, newObject, HttpMethod.POST, params);
    }
    
    @Override
    public RestObject createContent(RestObject object, Object content, String mediaType, String... params) {
        return post(object.getHref(CONTENTS), content, mediaType, JsonObject.class, params);
    }
    
    @Override
    public RestObject getPrimaryContent(RestObject object, String... params) {
        return getContent(object.getHref(PRIMARY_CONTENT), params);
    }
    
    @Override
    public RestObject getContent(String contentUri, String... params) {
        return get(contentUri, false, JsonObject.class, params);
    }
    
    @Override
    public Feed<RestObject> getContents(RestObject object, String... params) {
        Feed<? extends RestObject> feed = get(object.getHref(CONTENTS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject checkout(RestObject object, String... params) {
        return put(object.getHref(CHECKOUT), JsonObject.class, params);
    }
    
    @Override
    public void cancelCheckout(RestObject object) {
        delete(object.getHref(CANCEL_CHECKOUT));
    }
    
    @Override
    public RestObject checkinNextMajor(RestObject oldObject, RestObject newObject, String... params) {
        return post(oldObject.getHref(CHECKIN_NEXT_MAJOR), new JsonObject(newObject), JsonObject.class, params);
    }
    
    @Override
    public RestObject checkinNextMajor(RestObject oldObject, RestObject newObject, Object content, String contentMediaType, String... params) {
        return post(oldObject.getHref(CHECKIN_NEXT_MAJOR), newObject==null?null:new JsonObject(newObject), content, contentMediaType, JsonObject.class, params);
    }

    @Override
    public RestObject checkinNextMajor(RestObject oldObject, RestObject newObject, List<Object> contents,
            List<String> contentMediaTypes, String... params) {
        return post(oldObject.getHref(CHECKIN_NEXT_MAJOR), newObject==null?null:new JsonObject(newObject), contents, contentMediaTypes, JsonObject.class, params);
    }

    @Override
    public RestObject checkinNextMinor(RestObject oldObject, RestObject newObject, String... params) {
        return post(oldObject.getHref(CHECKIN_NEXT_MINOR), new JsonObject(newObject), JsonObject.class, params);
    }

    @Override
    public RestObject checkinNextMinor(RestObject oldObject, RestObject newObject, Object content, String contentMediaType, String... params) {
        return post(oldObject.getHref(CHECKIN_NEXT_MINOR), newObject==null?null:new JsonObject(newObject), content, contentMediaType, JsonObject.class, params);
    }
    
    @Override
    public RestObject checkinNextMinor(RestObject oldObject, RestObject newObject, List<Object> contents, List<String> contentMediaTypes, String... params) {
        return post(oldObject.getHref(CHECKIN_NEXT_MINOR), newObject==null?null:new JsonObject(newObject), contents, contentMediaTypes, JsonObject.class, params);
    }

    @Override
    public RestObject checkinBranch(RestObject oldObject, RestObject newObject, String... params) {
        return post(oldObject.getHref(CHECKIN_BRANCH_VERSION), new JsonObject(newObject), JsonObject.class, params);
    }
    
    @Override
    public RestObject checkinBranch(RestObject oldObject, RestObject newObject, Object content, String contentMediaType, String... params) {
        return post(oldObject.getHref(CHECKIN_BRANCH_VERSION), newObject==null?null:new JsonObject(newObject), content, contentMediaType, JsonObject.class, params);
    }

    @Override
    public RestObject checkinBranch(RestObject oldObject, RestObject newObject, List<Object> contents,
            List<String> contentMediaTypes, String... params) {
        return post(oldObject.getHref(CHECKIN_BRANCH_VERSION), newObject==null?null:new JsonObject(newObject), contents, contentMediaTypes, JsonObject.class, params);
    }

    @Override
    public Feed<RestObject> getVersions(RestObject object, String... params) {
        Feed<? extends RestObject> feed = get(object.getHref(VERSIONS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject materialize(RestObject oldObject) {
        return put(oldObject.getHref(MATERIALIZE), JsonObject.class);
    }

    @Override
    public void dematerialize(RestObject oldObject) {
        delete(oldObject.getHref(DEMATERIALIZE));
    }

    @Override
    public RestObject reparent(RestObject oldObject, RestObject newParent) {
        JsonObject parent = new JsonObject();
        parent.setHref(newParent.getHref(SELF));
        return post(oldObject.getHref(SHARED_PARENT), parent, JsonObject.class);
    }
    
    @Override
    public RestType getType(String name, String... params) {
        return get(getRepository().getHref(TYPES)+"/"+name, false, JsonType.class, params);
    }
    
    @Override
    public Feed<RestType> getTypes(String... params) {
        Repository repository = getRepository();
        String typesUri = repository.getHref(TYPES);
        Feed<? extends RestType> feed = get(typesUri, true, JsonFeeds.TypeFeed.class, params);
        return (Feed<RestType>)feed;
    }
    
    @Override
    public Feed<RestObject> getAspectTypes(String... params) {
        Repository repository = getRepository();
        String aspectTypesUri = repository.getHref(ASPECT_TYPES);
        Feed<? extends RestObject> feed = get(aspectTypesUri, true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }

    @Override
    public RestObject getAspectType(String aspectType, String... params) {
        return get(getRepository().getHref(ASPECT_TYPES)+"/"+aspectType, false, JsonObject.class, params);
    }

    @Override
    public ValueAssistant getValueAssistant(RestType type, ValueAssistantRequest request, String... params) {
        return post(type.getHref(ASSIS_VALUES), new JsonValueAssistantRequest(request), JsonValueAssistance.class, params);
    }
    
    @Override
    public ObjectAspects attach(RestObject object, String... aspects) {
        return post(object.getHref(OBJECT_ASPECTS), new JsonObjectAspects(aspects), JsonObjectAspects.class);        
    }

    @Override
    public void detach(ObjectAspects objectAspects, String aspect) {
        delete(objectAspects.getHref(DELETE, aspect));
    }
    
    @Override
    public Feed<RestObject> getUsers(String... params) {
        return getUsers(getRepository(), params);
    }
    
    @Override
    public Feed<RestObject> getUsers(Linkable parent, String... params) {
        Feed<? extends RestObject> feed = get(parent.getHref(USERS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }

    @Override
    public Feed<RestObject> getGroups(String... params) {
        Feed<? extends RestObject> feed = get(getRepository().getHref(GROUPS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject getCurrentUser(String... params) {
        return get(getRepository().getHref(LinkRelation.CURRENT_USER), false, JsonObject.class, params);
    }
    
    @Override
    public RestObject getDefaultFolder(String... params) {
        return get(getCurrentUser().getHref(LinkRelation.DEFAULT_FOLDER), false, JsonObject.class, params);
    }

    @Override
    public RestObject getUser(String userUri, String... params) {
        return get(userUri, false, JsonObject.class, params);
    }
    
    @Override
    public RestObject getGroup(String groupUri, String... params) {
        return get(groupUri, false, JsonObject.class, params);
    }

    @Override
    public RestObject createUser(RestObject userToCreate) {
        return post(getRepository().getHref(USERS), new JsonObject(userToCreate), JsonObject.class);
    }
    
    @Override
    public RestObject createGroup(RestObject groupToCreate) {
        return post(getRepository().getHref(GROUPS), new JsonObject(groupToCreate), JsonObject.class);
    }

    @Override
    public void addUserToGroup(RestObject group, RestObject user) {
        JsonObject groupUser = new JsonObject();
        groupUser.setHref(user.getHref(SELF));
        post(group.getHref(USERS), groupUser, null);
    }
     
    @Override
    public Feed<RestObject> getRelationTypes(String... params) {
        Feed<? extends RestObject> feed = get(getRepository().getHref(RELATION_TYPES), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject getRelationType(String uri, String... params) {
        return get(uri, false, JsonObject.class, params);
    }
    
    @Override
    public Feed<RestObject> getRelations(String... params) {
        Feed<? extends RestObject> feed = get(getRepository().getHref(RELATIONS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject getRelation(String uri, String... params) {
        return get(uri, false, JsonObject.class, params);
    }
    
    @Override
    public RestObject createRelation(RestObject object) {
        return post(getRepository().getHref(RELATIONS), new JsonObject(object), JsonObject.class);
    }
    
    @Override
    public Feed<RestObject> getFormats(String... params) {
        Feed<? extends RestObject> feed = get(getRepository().getHref(FORMATS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject getFormat(String uri, String... params) {
        return get(uri, false, JsonObject.class, params);
    }

    @Override
    public Feed<RestObject> getNetworkLocations(String... params) {
        Feed<? extends RestObject> feed = get(getRepository().getHref(NETWORK_LOCATIONS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject getNetworkLocation(String uri, String... params) {
        return get(uri, false, JsonObject.class, params);
    }
    
    @Override
    public Feed<FolderLink> getFolderLinks(Linkable object, LinkRelation rel, String... params) {
        Feed<? extends FolderLink> feed = get(object.getHref(rel), true, JsonFeeds.FolderLinkFeed.class, params);
        return (Feed<FolderLink>)feed;
    }
    
    @Override
    public FolderLink getFolderLink(String uri, String... params) {
        return get(uri, false, JsonFolderLink.class, params);
    }

    @Override
    public FolderLink move(FolderLink oldLink, FolderLink newLink, String... params) {
        return put(oldLink.getHref(SELF), new JsonFolderLink(newLink), JsonFolderLink.class, params);
    }
    
    @Override
    public FolderLink link(Linkable object, LinkRelation rel, FolderLink link) {
        return post(object.getHref(rel), new JsonFolderLink(link), JsonFolderLink.class);
    }
    
    @Override
    public Feed<RestObject> getAcls(String... params) {
        Feed<? extends RestObject> feed = get(getRepository().getHref(ACLS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public Feed<RestObject> getAclAssociations(Linkable acl, String... params) {
        Feed<? extends RestObject> feed = get(acl.getHref(ASSOCIATIONS), true, JsonFeeds.ObjectFeed.class, params);
        return (Feed<RestObject>)feed;
    }
    
    @Override
    public RestObject getAcl(String uri, String... params) {
        return get(uri, false, JsonObject.class, params);
    }
    
    @Override
    public RestObject createAcl(RestObject object) {
        return post(getRepository().getHref(ACLS), new JsonObject(object), JsonObject.class);
    }
    
    @Override
    public Capabilities getBatchCapabilities() {
        return get(getRepository().getHref(BATCH_CAPABILITIES), false, JsonBatchCapabilities.class);
    }

    @Override
    public Batch createBatch(Batch batch) {
        return post(batch, JsonBatch.class);
    }

    @Override
    public Feed<Preference> getPreferences(String... params) {
        Feed<? extends Preference> feed = get(getRepository().getHref(CURRENT_USER_PREFERENCES), true, JsonFeeds.PreferenceFeed.class, params);
        return (Feed<Preference>)feed;
    }

    @Override
    public Preference getPreference(String uri, String... params) {
        return get(uri, false, JsonPreference.class, params);
    }

    @Override
    public Preference createPreference(Preference preference) {
        return post(getRepository().getHref(CURRENT_USER_PREFERENCES), new JsonPreference(preference), JsonPreference.class);
    }

    @Override
    public Preference updatePreference(Preference oldPreference, Preference newPreference) {
        return post(oldPreference.self(), new JsonPreference(newPreference), JsonPreference.class);
    }
    
    @Override
    public Permission getPermission(Linkable linkable, String... params) {
        return get(linkable.getHref(LinkRelation.PERMISSIONS), false, JsonPermission.class, params);
    }

    @Override
    public PermissionSet getPermissionSet(Linkable linkable, String... params) {
        return get(linkable.getHref(LinkRelation.PERMISSION_SET), false, JsonPermissionSet.class, params);
    }

    @Override
    public Feed<Comment> getComments(Linkable parent, String... params) {
        Feed<? extends Comment> feed = get(parent.getHref(COMMENTS), true, JsonFeeds.CommentFeed.class, params);
        return (Feed<Comment>)feed;
    }

    @Override
    public Comment createComment(Linkable parent, Comment comment) {
        return post(parent.getHref(COMMENTS), new JsonComment(comment), JsonComment.class);
    }

    @Override
    public Comment getComment(String commentUri, String... params) {
        return get(commentUri, false, JsonComment.class, params);
    }

    @Override
    public Feed<Comment> getReplies(Linkable parent, String... params) {
        Feed<? extends Comment> feed = get(parent.getHref(REPLIES), true, JsonFeeds.CommentFeed.class, params);
        return (Feed<Comment>)feed;
    }

    @Override
    public Comment createReply(Linkable parent, Comment comment) {
        return post(parent.getHref(REPLIES), new JsonComment(comment), JsonComment.class);
    }

    @Override
    public Feed<VirtualDocumentNode> getVirtualDocumentNodes(Linkable linkable, String... params) {
        Feed<? extends VirtualDocumentNode> feed = get(linkable.getHref(VIRTUAL_DOCUMENT_NODES), true, JsonFeeds.VirtualDocumentNodeFeed.class, params);
        return (Feed<VirtualDocumentNode>)feed;
    }
    
    @Override
    public Feed<SearchTemplate> getSearchTemplates(String... params) {
        Feed<? extends SearchTemplate> feed = get(getRepository().getHref(SEARCH_TEMPLATES), true, JsonFeeds.SearchTemplateFeed.class, params);
        return (Feed<SearchTemplate>)feed;
    }

    @Override
    public SearchTemplate getSearchTemplate(String uri, String... params) {
        return get(uri, false, JsonSearchTemplate.class, params);
    }

    @Override
    public SearchTemplate createSearchTemplate(SearchTemplate template) {
        return post(getRepository().getHref(SEARCH_TEMPLATES), new JsonSearchTemplate(template), JsonSearchTemplate.class);
    }
    
    @Override
    public SearchFeed<RestObject> executeSearchTemplate(SearchTemplate toBeExecuted, String... params) {
        SearchFeed<? extends RestObject> feed = post(toBeExecuted.getHref(SEARCH_EXECUTION), toBeExecuted, JsonFeeds.SearchFeed.class, params);
        return (SearchFeed<RestObject>)feed;
    }

    @Override
    public SearchFeed<RestObject> executeSavedSearch(SavedSearch toBeExecuted, String... params) {
        SearchFeed<? extends RestObject> feed = get(toBeExecuted.getHref(SEARCH_EXECUTION), JsonFeeds.SearchFeed.class, params);
        return (SearchFeed<RestObject>)feed;
    }
    
    @Override
    public Feed<SavedSearch> getSavedSearches(String... params) {
        Feed<? extends SavedSearch> feed = get(getRepository().getHref(SAVED_SEARCHES), true, JsonFeeds.SavedSearchFeed.class, params);
        return (Feed<SavedSearch>)feed;
    }

    @Override
    public SavedSearch getSavedSearch(String uri, String... params) {
        return get(uri, false, JsonSavedSearch.class, params);
    }

    @Override
    public SavedSearch createSavedSearch(SavedSearch savedSearch) {
        return post(getRepository().getHref(SAVED_SEARCHES), new JsonSavedSearch(savedSearch), JsonSavedSearch.class);
    }
    
    @Override
    public SavedSearch updateSavedSearch(SavedSearch oldSavedSearch, SavedSearch newSavedSearch) {
        return post(oldSavedSearch.self(), new JsonSavedSearch(newSavedSearch), JsonSavedSearch.class);
    }

    @Override
    public SearchFeed<RestObject> enableSavedSearchResult(SavedSearch toBeExecuted, String... params) {
        SearchFeed<? extends RestObject> feed = put(toBeExecuted.getHref(SAVED_SEARCH_SAVED_RESULTS), JsonFeeds.SearchFeed.class, params);
        return (SearchFeed<RestObject>)feed;
    }

    @Override
    public void disableSavedSearchResult(SavedSearch toBeExecuted) {
        delete(toBeExecuted.getHref(SAVED_SEARCH_SAVED_RESULTS));
    }

    @Override
    public SearchFeed<RestObject> getSavedSearchResult(SavedSearch toBeExecuted, String... params) {
        SearchFeed<? extends RestObject> feed = get(toBeExecuted.getHref(SAVED_SEARCH_SAVED_RESULTS), true, JsonFeeds.SearchFeed.class, params);
        return (SearchFeed<RestObject>)feed;
    }

    @Override
    public <T extends Linkable> Feed<T> nextPage(Feed<T> feed) {
        return page(feed.getHref(PAGING_NEXT), feed.getClass());
    }
    
    @Override
    public <T extends Linkable> Feed<T> previousPage(Feed<T> feed) {
        return page(feed.getHref(PAGING_PREV), feed.getClass());
    }

    @Override
    public <T extends Linkable> Feed<T> firstPage(Feed<T> feed) {
        return page(feed.getHref(PAGING_FIRST), feed.getClass());
    }

    @Override
    public <T extends Linkable> Feed<T> lastPage(Feed<T> feed) {
        return page(feed.getHref(PAGING_LAST), feed.getClass());
    }
    
    private <T extends Linkable> Feed<T> page(String uri, Class<? extends Feed> clazz) {
        Feed<T> feed = null;
        if(uri != null) {
            feed = get(uri, true, clazz);
        }
        return feed;
    }

    @Override
    protected void initRestTemplate(RestTemplate restTemplate) {
        super.initRestTemplate(restTemplate);
        restTemplate.setErrorHandler(new DCTMJacksonErrorHandler(restTemplate.getMessageConverters()));
        for(HttpMessageConverter<?> c : restTemplate.getMessageConverters()) {
            if(c instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter)c).getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            } else if(c instanceof FormHttpMessageConverter) {
                try {
                    Field pcField = FormHttpMessageConverter.class.getDeclaredField("partConverters");
                    pcField.setAccessible(true);
                    List<HttpMessageConverter<?>> partConverters = (List<HttpMessageConverter<?>>)pcField.get(c);
                    for(HttpMessageConverter<?> pc : partConverters) {
                        if(pc instanceof MappingJackson2HttpMessageConverter) {
                            ((MappingJackson2HttpMessageConverter)pc).getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                            break;
                        }
                    }
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    @Override
    public void serialize(Object object, OutputStream os) {
        try {
            DCTMJacksonMapper.marshal(os, object);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public ClientType getClientType() {
        return ClientType.JSON;
    }
}
