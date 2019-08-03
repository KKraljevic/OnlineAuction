import { TestBed } from '@angular/core/testing';

import { UserInfo.ResolveService } from './user-info.resolve.service';

describe('UserInfo.ResolveService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserInfo.ResolveService = TestBed.get(UserInfo.ResolveService);
    expect(service).toBeTruthy();
  });
});
