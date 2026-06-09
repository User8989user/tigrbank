package com.tigrbank.di;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DIContainerTest {
    interface Service {}
    static class ServiceImpl implements Service {}
    static class Dependent {
        private final Service service;
        Dependent(Service service) { this.service = service; }
    }

    @Test
    void registerAndResolve_Transient_ReturnsNewInstance() {
        DIContainer container = new DIContainer();
        container.register(Service.class, ServiceImpl::new);
        Service s1 = container.resolve(Service.class);
        Service s2 = container.resolve(Service.class);
        assertNotNull(s1);
        assertNotNull(s2);
        assertNotSame(s1, s2);
    }

    @Test
    void registerSingleton_ReturnsSameInstance() {
        DIContainer container = new DIContainer();
        ServiceImpl instance = new ServiceImpl();
        container.registerSingleton(Service.class, instance);
        Service s1 = container.resolve(Service.class);
        Service s2 = container.resolve(Service.class);
        assertSame(s1, s2);
        assertSame(instance, s1);
    }

    @Test
    void resolve_NoBinding_ThrowsException() {
        DIContainer container = new DIContainer();
        assertThrows(RuntimeException.class, () -> container.resolve(Service.class));
    }

    @Test
    void resolve_DependentWithConstructorInjection_Works() {
        DIContainer container = new DIContainer();
        container.register(Service.class, ServiceImpl::new);
        container.register(Dependent.class, () -> new Dependent(container.resolve(Service.class)));
        Dependent d = container.resolve(Dependent.class);
        assertNotNull(d);
        assertNotNull(d.service);
    }
}