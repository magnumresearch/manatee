'use strict';

describe('Controller Tests', function() {

    describe('Queue Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockQueue, MockPatient, MockTeam;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockQueue = jasmine.createSpy('MockQueue');
            MockPatient = jasmine.createSpy('MockPatient');
            MockTeam = jasmine.createSpy('MockTeam');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Queue': MockQueue,
                'Patient': MockPatient,
                'Team': MockTeam
            };
            createController = function() {
                $injector.get('$controller')("QueueDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'manateeApp:queueUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
