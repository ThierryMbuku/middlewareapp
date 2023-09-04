package prosense.sassa.srdeft.transmissionfile.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.transmissionfile.entity.TransmissionFile;

import javax.enterprise.context.Dependent;

import java.util.*;

import javax.inject.Inject;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;


@Dependent
public class TransmissionFileAgent {
    @Inject
    TransmissionFileStore transmissionFileStore;

    public static final String id = "id";
    public static final String fileName = "fileName";
    public static final String service = "service";
    public static final String subService = "subService";
    public static final String type = "type";
    public static final String ackStatus = "ackStatus";
    public static final String content = "content";
    public static final String errorCode = "errorCode";
    public static final String test = "test";
    public static final String creator = "creator";
    public static final String created = "created";
    public static final String from = "from";
    public static final String to = "to";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        Optional.ofNullable(objectNode.get(fileName)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(fileName + " invalid");
            } else {
                if (n.asText().length() > 8) {
                    messages.add(fileName + " max length 8");
                }
            }
        });
        Optional.ofNullable(objectNode.get(service)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(service + " invalid");
            } else {
                try {
                    TransmissionFile.Service.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(service + " invalid: " + Arrays.toString(TransmissionFile.Service.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(subService)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(subService + " invalid");
            } else {
                try {
                    TransmissionFile.SubService.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(subService + " invalid: " + Arrays.toString(TransmissionFile.SubService.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(type)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(type + " invalid");
            } else {
                try {
                    TransmissionFile.Type.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(type + " invalid: " + Arrays.toString(TransmissionFile.Type.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(ackStatus)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(ackStatus + " invalid");
            } else {
                try {
                    TransmissionFile.AckStatus.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(ackStatus + " invalid: " + Arrays.toString(TransmissionFile.AckStatus.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(errorCode)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(errorCode + " invalid");
            } else {
                if (n.asText().length() > 20) {
                    messages.add("cannot select more than 10 " + errorCode + " options");
                }
            }
        });
        Optional.ofNullable(objectNode.get(test)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isBoolean()) {
                messages.add(test + " invalid");
            }
        });
        if (!messages.isEmpty()) {
            throw AppException.builder().badRequest400().messages(messages).build();
        }
    }

    public void validateCreate(final TransmissionFile transmissionFile) {
        final Set<String> messages = Sets.newHashSet();
        if (transmissionFile.getId() != null) {
            messages.add(id + " invalid");
        }
        if (transmissionFile.getService() == null) {
            messages.add(service + " mandatory");
        }
        if (transmissionFile.getType() == null) {
            messages.add(type + " mandatory");
        }
        if (transmissionFile.getTest() == null) {
            messages.add(test + " mandatory");
        }
        if (transmissionFile.getType() != null) {
            if (transmissionFile.getType().equals(TransmissionFile.Type.ctl_begin.name())) {
                messages.add(type + " " + TransmissionFile.Type.ctl_begin.name() + " not allowed");
            } else if (transmissionFile.getType().equals(TransmissionFile.Type.ctl_end.name())) {
                if(!exists(transmissionFile.toBuilder().type(TransmissionFile.Type.ctl_begin.name()).build())) {
                	messages.add("begin file for date " + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now()) + " does not exist");
                }
                if(exists(transmissionFile.toBuilder().type(TransmissionFile.Type.ctl_end.name()).build())) {
                	messages.add("end file for date " + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now()) + " already exists");
                }
                if (transmissionFile.getSubService() == null) {
                    messages.add(subService + " mandatory for type " + TransmissionFile.Type.ctl_end.name());
                }
                if (transmissionFile.getFileName() != null) {
                    messages.add(fileName + " should be null for type " + TransmissionFile.Type.ctl_end.name());
                }
                if (transmissionFile.getAckStatus() != null) {
                    messages.add(ackStatus + " should be null for type " + TransmissionFile.Type.ctl_end.name());
                }
                if (transmissionFile.getErrorCode() != null) {
                    messages.add(errorCode + " should be null for type " + TransmissionFile.Type.ctl_end.name());
                }
            } else {
                if (transmissionFile.getAckStatus() == null) {
                    messages.add(ackStatus + " mandatory for ack");
                } else {
                	if(transmissionFile.getAckStatus().equals(TransmissionFile.AckStatus.positive.name())) {
		                if (transmissionFile.getErrorCode() != null) {
        		            messages.add(errorCode + " should be null for ackStatus " + TransmissionFile.AckStatus.positive.name());
                		}
                	} else {
		                if (transmissionFile.getErrorCode() == null) {
        		            messages.add(errorCode + " mandatory for ackStatus " + TransmissionFile.AckStatus.negative.name());
                		}
                	}
                }
                if (transmissionFile.getType().equals(TransmissionFile.Type.ack_begin.name())) {
                    if (transmissionFile.getSubService() != null) {
                        messages.add(subService + " should be null for type " + TransmissionFile.Type.ack_begin.name());
                    }
                    if (transmissionFile.getFileName() != null) {
                        messages.add(fileName + " should be null for type " + TransmissionFile.Type.ack_begin.name());
                    }
                } else if (transmissionFile.getType().equals(TransmissionFile.Type.ack_end.name())) {
                    if (transmissionFile.getSubService() != null) {
                        messages.add(subService + " should be null for type " + TransmissionFile.Type.ack_end.name());
                    }
                    if (transmissionFile.getFileName() != null) {
                        messages.add(fileName + " should be null for type " + TransmissionFile.Type.ack_end.name());
                    }
                } else {
                    if (transmissionFile.getFileName() == null) {
                        messages.add(fileName + " mandatory for type " + TransmissionFile.Type.ack_vet.name());
                    }
                    if (transmissionFile.getSubService() != null) {
                        messages.add(subService + " should be null for type " + TransmissionFile.Type.ack_vet.name());
                    }
                }
            }
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public boolean exists(final TransmissionFile transmissionFile) {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        if(transmissionFile.getFileName() != null)
	        queryParameters.add(fileName, transmissionFile.getFileName());
        queryParameters.add(service, String.valueOf(transmissionFile.getService()));
        if(transmissionFile.getSubService() != null)
	        queryParameters.add(subService, transmissionFile.getSubService());
        queryParameters.add(type, transmissionFile.getType());
        if(transmissionFile.getAckStatus() != null)
    	    queryParameters.add(ackStatus, transmissionFile.getAckStatus());
        queryParameters.add(test, String.valueOf(transmissionFile.getTest()));
        queryParameters.add(from, ZonedDateTime.now().with(LocalTime.MIN).format(DateTimeFormatter.ISO_INSTANT));
        queryParameters.add(to, ZonedDateTime.now().with(LocalTime.MAX).format(DateTimeFormatter.ISO_INSTANT));
        Set<TransmissionFile> transmissionFiles = transmissionFileStore.search(queryParameters);
        if (transmissionFiles.size() == 0)
            return false;
        else
        	return true;
    }
    
    public void validateIfExists(final TransmissionFile transmissionFile) {
        final Set<String> messages = Sets.newHashSet();
        if (exists(transmissionFile)) {
            messages.add("transmissionFile already exists for given parameters");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }
    
    public TransmissionFile forCreate(final ObjectNode objectNode, final String user) {
        final TransmissionFile.TransmissionFileBuilder builder = TransmissionFile.builder();
        Optional.ofNullable(objectNode.get(fileName)).map(JsonNode::asText).ifPresent(builder::fileName);
        Optional.ofNullable(objectNode.get(service)).map(JsonNode::asText).ifPresent(builder::service);
        Optional.ofNullable(objectNode.get(subService)).map(JsonNode::asText).ifPresent(builder::subService);
        Optional.ofNullable(objectNode.get(type)).map(JsonNode::asText).ifPresent(builder::type);
        Optional.ofNullable(objectNode.get(ackStatus)).map(JsonNode::asText).ifPresent(builder::ackStatus);
        Optional.ofNullable(objectNode.get(errorCode)).map(JsonNode::asText).ifPresent(builder::errorCode);
        Optional.ofNullable(objectNode.get(test)).map(JsonNode::asBoolean).ifPresent(builder::test);
        builder.creator(user);
        builder.created(ZonedDateTime.now());
        return builder.build();
    }

    public Map<String, Object> toMap(final TransmissionFile transmissionFile) {
        return toFilteredMap(transmissionFile, ImmutableSet.of(fileName, service, subService, type, ackStatus, content, errorCode, test, creator, created));
    }

    public Map<String, Object> toFilteredMap(final TransmissionFile transmissionFile, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, transmissionFile.getId());
        fields.forEach(f -> {
            if (fileName.equals(f)) {
                map.put(fileName, transmissionFile.getFileName());
            }
            if (service.equals(f)) {
                map.put(service, transmissionFile.getService());
            }
            if (subService.equals(f)) {
                map.put(subService, transmissionFile.getSubService());
            }
            if (type.equals(f)) {
                map.put(type, transmissionFile.getType());
            }
            if (ackStatus.equals(f)) {
                map.put(ackStatus, transmissionFile.getAckStatus());
            }
            if (content.equals(f)) {
                map.put(content, transmissionFile.getContent());
            }
            if (errorCode.equals(f)) {
                map.put(errorCode, transmissionFile.getErrorCode());
            }
            if (test.equals(f)) {
                map.put(test, transmissionFile.getTest());
            }
            if (creator.equals(f)) {
                map.put(creator, transmissionFile.getCreator());
            }
            if (created.equals(f)) {
                map.put(created, transmissionFile.getCreated());
            }
        });
        return map;
    }
}
